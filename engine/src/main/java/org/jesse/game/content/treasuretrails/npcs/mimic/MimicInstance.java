package org.jesse.game.content.treasuretrails.npcs.mimic;

import org.jesse.game.content.ItemRetrievalService;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.content.treasuretrails.ClueLevel;
import org.jesse.game.content.treasuretrails.rewards.ClueReward;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Colour;
import org.jesse.game.world.Position;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.region.DynamicArea;
import org.jesse.game.world.region.area.plugins.DeathPlugin;
import org.jesse.game.world.region.area.plugins.DropPlugin;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException;
import org.jesse.logger.NearRealityLogger;
import org.jesse.plugins.dialogue.PlainChat;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

import static org.jesse.game.world.entity.player.Player.DEATH_ANIMATION;

/**
 * @author Kris | 26/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MimicInstance extends DynamicArea implements DropPlugin, DeathPlugin, LootBroadcastPlugin {
    private static final Logger log = NearRealityLogger.getLogger(MimicInstance.class);
    public static final Location strangeCasketLocation = new ImmutableLocation(1641, 3578, 1);
    private static final Location entrance = new ImmutableLocation(2719, 4314, 1);
    private static final Location mimicSpawn = new ImmutableLocation(2718, 4319, 1);
    private static final int CHUNK_X = 338;
    private static final int CHUNK_Y = 538;
    private static final int WIDTH = 4;
    private static final int HEIGHT = 4;

    public static void build(@NotNull final Player player) {
        try {
            final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(WIDTH, HEIGHT);
            final MimicInstance instance = new MimicInstance(player, allocatedArea);
            instance.constructRegion();
            player.stop(Player.StopType.WALK, Player.StopType.INTERFACES, Player.StopType.ACTIONS, Player.StopType.ROUTE_EVENT);
            new FadeScreen(player, () -> player.setLocation(instance.getLocation(entrance))).fade(3);
        } catch (OutOfSpaceException e) {
            log.error("", e);
        }
    }

    private final String username;

    private MimicInstance(@NotNull final Player player, @NotNull AllocatedArea allocatedArea) {
        super(allocatedArea, CHUNK_X, CHUNK_Y);
        this.username = player.getUsername();
    }

    @Override
    public boolean drop(final Player player, final Item item) {
        if (item.getId() == ItemId.MIMIC) {
            player.sendMessage("You shouldn't drop that in here.");
            return false;
        }
        return true;
    }

    @Override
    public void constructed() {
        new Mimic(getLocation(mimicSpawn), this).spawn();
    }

    @Override
    public void enter(Player player) {
        player.getDialogueManager().start(new PlainChat(player, "If you " + Colour.RS_RED.wrap("die") + " in here, youre items can be retrieved from the " + Colour.RS_RED.wrap("Strange Casket") + " in Watson's house, for a " + Colour.RS_RED.wrap("fee of 90,000 coins") + ". Anything else you leave in here will be " + Colour.RS_RED.wrap("lost permanently") + "."));
        final Item item = CollectionUtils.findMatching(player.getInventory().getContainer().getItems().values(), it -> it.getId() == ItemId.MIMIC);
        if (item != null) {
            final int rolls = item.getNumericAttribute("The Mimic rolls").intValue();
            player.sendMessage(Colour.RED.wrap("You have " + rolls + " chance" + (rolls == 1 ? "" : "s") + " to defeat the Mimic."));
        }
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public Location onLoginLocation() {
        return strangeCasketLocation;
    }

    public void finish() {
        player().ifPresent(player -> {
            final Item item = CollectionUtils.findMatching(player.getInventory().getContainer().getItems().values(), it -> it.getId() == ItemId.MIMIC);
            if (item != null) {
                item.setAttribute("The Mimic slain", 1);
                final int rolls = item.getNumericAttribute("The Mimic rolls").intValue();
                Number originalCasket = item.getNumericAttribute("The Mimic original casket");
                int originalId;
                if(originalCasket == null) {
                    originalId = 20543;
                } else originalId = originalCasket.intValue();
                final ClueLevel tier = Objects.requireNonNull(ClueReward.getTier(originalId));
                final int startRolls = tier == ClueLevel.MASTER ? 6 : 5;
                player.sendMessage(Colour.RED.wrap("Remaining loot rolls: " + rolls + "/" + startRolls));
            }
        });
    }

    final Optional<Player> player() {
        return World.getPlayer(username);
    }

    @Override
    public String name() {
        return username + "'s Mimic Instance";
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return "Mimic";
    }

    @Override
    public Location getRespawnLocation() {
        return new Location(3087, 3490, 0);
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        player.setAnimation(Animation.STOP);
        player.lock();
        player.stopAll();
        if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
            player.getPrayerManager().applyRetributionEffect(source);
        }
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (player.isFinished() || player.isNulled()) {
                    stop();
                    return;
                }
                if (ticks == 0) {
                    player.setAnimation(DEATH_ANIMATION);
                } else if (ticks == 2) {
                    player.sendMessage("Oh dear, you have died.");
                    player.sendMessage("The Mimic has rejected some of your items. You can collect them from the Strange Casket at Watson's");
                    player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.MIMIC, source, true);
                    player.reset();
                    ItemRetrievalService.updateVarps(player);
                    if (player.getVariables().isSkulled()) {
                        player.getVariables().setSkull(false);
                    }
                    player.unlock();
                    player.setLocation(new Location(3100, 3498, 0));
                } else if (ticks == 3) {
                    player.unlock();
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(Animation.STOP);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
        return true;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
