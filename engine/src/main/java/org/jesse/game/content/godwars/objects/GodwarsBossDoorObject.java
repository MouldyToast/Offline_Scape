package org.jesse.game.content.godwars.objects;

import org.jesse.game.content.clans.ClanChannel;
import org.jesse.game.content.clans.ClanManager;
import org.jesse.game.content.clans.ClanRank;
import org.jesse.game.content.godwars.GodwarsInstanceManager;
import org.jesse.game.content.godwars.GodwarsInstancePortal;
import org.jesse.game.content.godwars.instance.GodwarsInstance;
import org.jesse.game.content.godwars.instance.InstanceConstants;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.privilege.MemberRank;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.RegionArea;
import org.jesse.game.world.region.area.godwars.*;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException;
import org.jesse.utils.TextUtils;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GodwarsBossDoorObject implements ObjectAction {
    private static final Logger log = LoggerFactory.getLogger(GodwarsBossDoorObject.class);
    private static final Class<?>[] INSTANCE_CTOR_PARAMS = new Class[] {String.class, AllocatedArea.class};

    public enum BossDoor {
        //@formatter:off
        BANDOS(26503, BandosChamberArea.class),
        ARMADYL(26502, ArmadylChamberArea.class),
        SARADOMIN(26504, SaradominChamberArea.class),
        ZAMORAK(26505, ZamorakChamberArea.class),
        ANCIENT(42934, "org.jesse.game.content.boss.nex.AncientChamberArea");
        //@formatter:on
        private static final List<BossDoor> values = Collections.unmodifiableList(Arrays.asList(values()));
        private final int objectId;
        private final Class<? extends GodwarsDungeonArea> clazz;
        private final String formattedName = TextUtils.capitalizeFirstCharacter(name().toLowerCase());

        BossDoor(int objectId, Class<? extends GodwarsDungeonArea> clazz) {
            this.objectId = objectId;
            this.clazz = clazz;
        }

        @SuppressWarnings("unchecked")
        BossDoor(int objectId, String clazz) {
            this.objectId = objectId;
            Class<? extends GodwarsDungeonArea> areaClass = null;
            try {
                areaClass = (Class<? extends GodwarsDungeonArea>) Class.forName(clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            this.clazz = areaClass;
        }

        public static List<BossDoor> getValues() {
            return values;
        }

        @Override
        public String toString() {
            return formattedName;
        }

        public int getObjectId() {
            return objectId;
        }

        public String getFormattedName() {
            return formattedName;
        }
    }

    private int calculateRequiredKillcount(@NotNull final Player player) {
        int requiredKillcount = 10;
        final MemberRank rank = player.getMemberRank();
        if (rank.equalToOrGreaterThan(MemberRank.DRAGONSTONE))
            requiredKillcount -= 10;
        else if (rank.equalToOrGreaterThan(MemberRank.DIAMOND))
            requiredKillcount -= 8;
        else if (rank.equalToOrGreaterThan(MemberRank.RUBY))
            requiredKillcount -= 6;
        else if (rank.equalToOrGreaterThan(MemberRank.EMERALD))
            requiredKillcount -= 4;
        else if (rank.equalToOrGreaterThan(MemberRank.SAPPHIRE))
            requiredKillcount -= 2;
        return requiredKillcount;
    }

    private void notifyChamberSize(@NotNull final Player player, final int size) {
        player.sendMessage("There " + (size == 1 ? "is" : "are") + " " + size + " adventurer" + (size == 1 ? "" : "s") + " inside the chamber.");
    }

    public static int getInstanceChamberCount(@NotNull final RegionArea area) {
        int count = 0;
        if (area instanceof GodwarsInstance instance) {
            final RSPolygon polygon = instance.chamberPolygon();
            count = (int) instance.getPlayers()
                .stream()
                .filter(p -> polygon.contains(p.getLocation()))
                .count();
        }
        return count;
    }

    private boolean insideChamber(@NotNull final Player player, @NotNull final BossDoor door) {
        final RegionArea area = player.getArea();
        if (area instanceof GodwarsInstance) {
            return (((GodwarsInstance) area).chamberPolygon().contains(player.getLocation()));
        }
        final Class<? extends GodwarsDungeonArea> clazz = door.clazz;
        return clazz != null && GlobalAreaManager.getArea(clazz).getPlayers().contains(player);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final GodwarsBossDoorObject.BossDoor door = Objects.requireNonNull(CollectionUtils.findMatching(BossDoor.getValues(), v -> v.getObjectId() == object.getId()));
        if (option.equals("Peek")) {
            final RegionArea playerArea = player.getArea();
            final int count = playerArea instanceof GodwarsInstance
                    ? getInstanceChamberCount(playerArea)
                    : GlobalAreaManager.getArea(door.clazz).getPlayers().size();
            notifyChamberSize(player, count);
            return;
        }

        //EDGE: ancient door has two areas, safe and unsafe
        //      this door is entering and exiting the safe one, so we do not need
        //      to pay when leaving and actually can leave.
        boolean insideChamber = this.insideChamber(player, door);
        boolean ancientDoor = door == GodwarsBossDoorObject.BossDoor.ANCIENT;

        if (!ancientDoor && insideChamber) {
            player.sendMessage("You cannot leave the boss room through this side of the door!");
            return;
        }

        if (insideChamber) {
            this.pass(player, object, door);
            return;
        }

        if (!this.pay(player, door)) {
            return;
        }

        // Killcount/key paid. In a private instance, go straight through.
        if (player.getArea() instanceof GodwarsInstance) {
            this.pass(player, object, door);
            return;
        }

        if (option.equals("Open (private)")) {
            final GodwarsInstancePortal portal = CollectionUtils.findMatching(
                    GodwarsInstancePortal.getValues(), p -> p.name().equals(door.name()));
            if (portal != null && portal.getInstanceClass() != null) {
                enterPrivateInstance(player, door, portal);
            }
            return;
        }

        // "Open" or "Open (normal)" — public entry.
        this.pass(player, object, door);
    }

    private boolean pay(Player player, GodwarsBossDoorObject.BossDoor door) {

        if (player.getInventory().containsAnyOf(ItemId.ECUMENICAL_KEY))
            return player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY, 1).getResult() == RequestResult.SUCCESS;

        final int requiredKillcount = calculateRequiredKillcount(player);
        final int killcount = player.getNumericAttribute(door.formattedName + "Kills").intValue();
        if (killcount < requiredKillcount && !player.getInventory().containsItem(ItemId.ECUMENICAL_KEY, 1)) {
            player.sendMessage("This door is locked by the power of " + door.formattedName + "! You will need to collect the essence of at least " + requiredKillcount + " of his followers before the door will open.");
            return false;
        }

        if (killcount >= requiredKillcount) {
            player.addAttribute(door.formattedName + "Kills", Math.max(0, killcount - requiredKillcount));
            GodwarsDungeonArea.refreshKillcount(player);
            player.sendMessage("The door devours the life-force of " + requiredKillcount + " followers of " + door.formattedName + " that you have slain.");
        } else {
            player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY, 1);
            player.sendMessage("The door devours the ecumenical key.");
        }
        return true;
    }

    private void pass(Player player, WorldObject object, GodwarsBossDoorObject.BossDoor door) {
        object.setLocked(true);
        final WorldObject obj = new WorldObject(object);
        obj.setRotation((obj.getRotation() - 1) & 3);
        World.spawnGraphicalDoor(obj);
        player.lock();
        player.setRunSilent(2);
        final boolean horizontal = (object.getRotation() & 1) == 0;
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                switch (ticks++) {
                    case 0:
                        final int destinationX = horizontal ? (player.getX() + (player.getX() < object.getX() ? 2 : -2)) : (player.getX());
                        final int destinationY = !horizontal ? (player.getY() + (player.getY() < object.getY() ? 2 : -2)) : (player.getY());
                        player.addWalkSteps(destinationX, destinationY, 2, false);
                        break;
                    case 1:
                        player.unlock();
                        World.spawnGraphicalDoor(object);
                        break;
                    case 2:
                        object.setLocked(false);
                        stop();
                        break;
                }
            }
        }, 0, 1);
    }

    private void enterPrivateInstance(Player player, BossDoor door, GodwarsInstancePortal portal) {
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null) {
            player.sendMessage("You need to be in a clan chat channel to start or join an instance.");
            return;
        }
        final Optional<GodwarsInstance> existing = GodwarsInstanceManager.getManager().findInstance(player, portal.getGod());
        if (existing.isPresent()) {
            player.lock(1);
            player.teleport(existing.get().getLocation(portal.getPortalLocation()));
            return;
        }
        final ClanRank rank = ClanManager.getRank(player, channel);
        if (rank.getId() < channel.getKickRank().getId()) {
            player.sendMessage("Clan members ranked as " + StringFormatUtil.formatString(channel.getKickRank().toString()) + " or above can only start a clan instance.");
            return;
        }
        final int cost = InstanceConstants.getInstanceCost(player, portal.getCost());
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("Pay " + StringFormatUtil.format(cost) + " to start a private instance?");
                options(new DialogueOption("Yes.", () -> {
                    long available = (long) player.getInventory().getAmountOf(ItemId.COINS_995) + player.getBank().getAmountOf(ItemId.COINS_995);
                    if (available < cost) {
                        player.sendMessage("You don't have enough coins with you or in your bank.");
                        return;
                    }
                    if (GodwarsInstanceManager.getManager().findInstance(player, portal.getGod()).isPresent()) {
                        player.sendMessage("Someone in your clan has already initiated an instance.");
                        return;
                    }
                    player.lock(1);
                    player.getInventory().deleteItem(new Item(995, cost)).onFailure(remainder -> player.getBank().remove(remainder));
                    try {
                        final int chunks = portal == GodwarsInstancePortal.ANCIENT ? 16 : 8;
                        final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(chunks, 8);
                        final GodwarsInstance area = portal.getInstanceClass()
                                .getDeclaredConstructor(INSTANCE_CTOR_PARAMS)
                                .newInstance(channel.getOwner(), allocatedArea);
                        area.constructRegion();
                        player.teleport(area.getLocation(portal.getPortalLocation()));
                    } catch (OutOfSpaceException | InstantiationException | InvocationTargetException |
                             NoSuchMethodException | IllegalAccessException e) {
                        log.error("Failed to create GWD instance for {}", door.getFormattedName(), e);
                    }
                }), new DialogueOption("No."));
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return Arrays.stream(BossDoor.values())
                .map(it -> (Object) it.objectId)
                .toArray();
    }
}
