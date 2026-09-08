package org.jesse.game.content.scar_essence_mine.object;

import org.jesse.game.item.Item;
import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.ObjectHandler;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.DoubleItemChat;
import org.jesse.plugins.dialogue.PlainChat;

import static org.jesse.game.world.entity.player.PlayerAttributesKt.getDepositedTaintedEssenceChunks;
import static org.jesse.game.item.ids.ItemId.*;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/18/2025
 */
public class EssenceOrifice extends Object implements ObjectAction {

    private static final Item TAINTED_ESSENCE_PLACEHOLDER = new Item(TAINTED_ESSENCE_CHUNK);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Runnable runnable = () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        };
        if (player.getLocation().getPositionHash() == object.getPositionHash()) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(player.getLocation())), runnable));
        } else {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), runnable));
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Withdraw")) {
            if (object.getId() == ObjectId.ESSENCE_ORIFICE) {
                player.getDialogueManager().start(
                        new Dialogue(player) {
                            @Override
                            public void buildDialogue() {
                                plain("This extractor converts 2 tainted essence into 1 pure essence.")
                                        .executeAction(() -> WorldTasksManager.schedule(getDelayTask(player)));
                            }
                        }
                );
            }
        }
    }

    private TickTask getDelayTask(Player player) {
        return new TickTask() {
            @Override
            public void run() {
                promptEssenceConversion(player);
                stop();

            }
        };
    }

    private void promptEssenceConversion(Player player) {
        var currentTaintedEssence = getDepositedTaintedEssenceChunks(player);
        int maxEssenceToWithdraw = currentTaintedEssence / 2;

        player.sendInputInt("How many pure essence would you like to receive? (0-" + maxEssenceToWithdraw + ")", amount -> {
            if (amount <= 0) {
                player.getDialogueManager().start(new PlainChat(player, "Invalid amount entered."));
                return;
            }
            if (amount > maxEssenceToWithdraw) {
                player.getDialogueManager().start(new PlainChat(player, "You don't have enough tainted essence to convert that many pure essence."));
                return;
            }
            int remainingEssence = currentTaintedEssence - (amount * 2);
            player.getAttributes().put("depositedTaintedEssenceChunks", remainingEssence);
            player.getInventory().addItem(new Item(PURE_ESSENCE_NOTED, amount));
            player.getDialogueManager().start(
                    new Dialogue(player, NpcId.HAGUS) {
                        @Override
                        public void buildDialogue() {
                            npc( "Excellent, Ventriculus shall enjoy that. I've noted the pure essence for you.")
                                    .executeAction(() -> WorldTasksManager.schedule(getConversionDelayTask(player, amount)));
                        }
                    });
        });
    }

    private TickTask getConversionDelayTask(Player player, int amount) {
        return new TickTask() {
            @Override
            public void run() {
                Item taintedEssenceItem = new Item(TAINTED_ESSENCE_CHUNK, amount * 2);
                player.getDialogueManager().start(new DoubleItemChat(player, taintedEssenceItem, new Item(PURE_ESSENCE, amount), "You commune with the Ventriculus and Hagus presents you with " + amount + " pure essence."));
                stop();

            }
        };
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.ESSENCE_ORIFICE};
    }
}
