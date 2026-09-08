package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.impl.motherlode.PaydirtNPC;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.CharacterLoop;
import org.jesse.plugins.dialogue.ItemChat;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class MotherlodePaydirtHopper implements ObjectAction {

    public static final Item PAYDIRT_ITEM = new Item(12011);

    private static final int PAYDIRT_NPC = 6564;

    private static final Location START = new Location(3748, 5671, 0);

    private static final Location northernHopper = new Location(3755, 5676, 0);

    private static final Location easternHopper = new Location(3748, 5673, 0);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        var usingLowerLevel = object.getLocation().withinDistance(3748, 5672, 1);
        if (usingLowerLevel) {
            final var strategy = getStrategy(object);
            final var runnable = getRunnable(player, object, name, optionId, option);
            final var delay = getDelay();
            final var objectEvent = new ObjectEvent(player, strategy, runnable, delay);
            player.setRouteEvent(objectEvent);
            return;
        }
        player.setRouteEvent(new TileEvent(player, new TileStrategy(northernHopper), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getInventory().containsItem(PAYDIRT_ITEM)) {
            player.getDialogueManager().start(new ItemChat(player, PAYDIRT_ITEM, "You don\'t have any pay-dirt to put in the hopper."));
            return;
        }
        final MutableInt conveyorAmount = new MutableInt();
        CharacterLoop.forEach(object, 15, NPC.class, npc -> {
            if (npc instanceof PaydirtNPC) {
                final Player target = ((PaydirtNPC) npc).getPlayer();
                if (target != null && !target.isNulled() && target.getUsername().equalsIgnoreCase(player.getUsername())) {
                    conveyorAmount.add(((PaydirtNPC) npc).getPaydirt().size());
                }
            }
        });
        if (conveyorAmount.intValue() > 0) {
            player.getDialogueManager().start(new ItemChat(player, PAYDIRT_ITEM, "You\'ve already got some pay-dirt in the machine.<br>You can put more in once the last batch comes out."));
            return;
        }
        final int MAX_AMOUNT = (player.getBooleanAttribute("motherlode_sack_upgrade") ? 162 : 81);
        int paydirt = player.getInventory().getAmountOf(PAYDIRT_ITEM.getId());
        final int sackAmount = player.getPaydirt().size();
        if (sackAmount + conveyorAmount.intValue() >= MAX_AMOUNT) {
            player.getDialogueManager().start(new ItemChat(player, PAYDIRT_ITEM, "You can\'t fit any more pay-dirt into the sack! Try emptying the sack first."));
            return;
        }
        final ArrayList<Item> list = new ArrayList<Item>();
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null || item.getId() != PAYDIRT_ITEM.getId()) {
                continue;
            }
            list.add(item);
            inventory.deleteItem(i, item);
            if (--paydirt <= 0) {
                break;
            }
        }
        World.spawnNPC(new PaydirtNPC(PAYDIRT_NPC, START, Direction.SOUTH, 0, player, list));
    }

    /**
     * Paydirt in sack management
     */
    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HOPPER_26674 };
    }
}
