package org.jesse.game.content.scar_essence_mine.object;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.ObjectHandler;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;

import static org.jesse.game.world.entity.player.PlayerAttributesKt.getDepositedTaintedEssenceChunks;
import static org.jesse.game.item.ids.ItemId.TAINTED_ESSENCE_CHUNK;


/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/18/2025
 */
public class VentriculusOpening implements ItemOnObjectAction, ObjectAction {

    private static final Item TAINTED_ESSENCE_PLACEHOLDER = new Item(28591, 1);


    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (item.getId() == TAINTED_ESSENCE_CHUNK && object.getId() == ObjectId.VENTRICULUS_OPENING) {
            performDeposit(player, object);
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option) {
            case "Deposit" -> {
                if (object.getId() == ObjectId.VENTRICULUS_OPENING) {
                    performDeposit(player, object);
                    int currentTaintedEssence = getDepositedTaintedEssenceChunks(player);
                    player.sendMessage("You deposit the tainted essence chunks into the Ventriculus.");
                    player.sendMessage("You have " + Colour.RED.wrap(Utils.format(currentTaintedEssence)) + " tainted essence stored within it.");
                }
            }
            case "Inspect" -> {
                if (object.getId() == ObjectId.VENTRICULUS_OPENING) {
                    int currentTaintedEssence = getDepositedTaintedEssenceChunks(player);
                    player.sendMessage("You have " + Colour.RED.wrap(Utils.format(currentTaintedEssence)) + " tainted essence stored within it.");
                    player.getDialogueManager().start(new ItemChat(player, TAINTED_ESSENCE_PLACEHOLDER, "The Ventriculus currently contains " + currentTaintedEssence + " tainted essence."));
                }
            }
        }
    }

    private void performDeposit(Player player, WorldObject object) {
        if (!player.getInventory().containsItem(28591, 1)) {
            player.getDialogueManager().start(new ItemChat(player, TAINTED_ESSENCE_PLACEHOLDER, "There are no tainted essence chunks in your inventory."));
            return;
        }
        int taintedEssenceCount = player.getInventory().getAmountOf(TAINTED_ESSENCE_CHUNK);
        int modifiedEssenceCount = taintedEssenceCount * 5;
        int currentTaintedEssence = getDepositedTaintedEssenceChunks(player);
        int MAX_TAINTED_ESSENCE = Integer.MAX_VALUE;
        int maxAddableEssence = MAX_TAINTED_ESSENCE - currentTaintedEssence;
        int amountToAdd = Math.min(maxAddableEssence, modifiedEssenceCount);

        if (player.getInventory().deleteItem(TAINTED_ESSENCE_CHUNK, taintedEssenceCount).getResult() == RequestResult.SUCCESS) {
            int newTaintedEssence = currentTaintedEssence + amountToAdd;
            player.getAttributes().put("depositedTaintedEssenceChunks", newTaintedEssence);
        }
    }

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
    public int getObjectStrategyDistance(WorldObject obj) {
        if (obj.getId() == ObjectId.VENTRICULUS_OPENING) {
            return 1;
        }

        return ItemOnObjectAction.super.getObjectStrategyDistance(obj);
    }

    @Override
    public Object[] getItems() { return new Object[] {TAINTED_ESSENCE_CHUNK};
    }

    @Override
    public Object[] getObjects() { return new Object[] {ObjectId.VENTRICULUS_OPENING};
    }
}
