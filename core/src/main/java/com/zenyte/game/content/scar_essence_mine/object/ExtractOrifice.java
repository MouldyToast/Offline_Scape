package com.zenyte.game.content.scar_essence_mine.object;

import com.zenyte.game.content.scar_essence_mine.dialogue.ExtractOrificeActionD;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class ExtractOrifice implements ObjectAction {


    private static final Item TAINTED_ESSENCE_PLACEHOLDER = new Item(28591, 1);
    private static final Item WARPED_EXTRACT_PLACEHOLDER = new Item(28593, 1);
    private static final Item TWISTED_EXTRACT_PLACEHOLDER = new Item(28595, 1);
    private static final Item MANGLED_EXTRACT_PLACEHOLDER = new Item(28597, 1);
    private static final Item SCARRED_EXTRACT_PLACEHOLDER = new Item(28599, 1);

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
    public ObjectStrategy getStrategy(WorldObject obj) {
        return ObjectAction.super.getStrategy(obj);
    }

    @Override
    public int getStrategyDistance(WorldObject obj) {
        return ObjectAction.super.getStrategyDistance(obj);
    }

    @Override
    public Runnable getRunnable(Player player, WorldObject object, String name, int optionId, String option) {
        return ObjectAction.super.getRunnable(player, object, name, optionId, option);
    }

    @Override
    public void init() {
        ObjectAction.super.init();
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option) {
            case "Withdraw":
                player.getDialogueManager().start(
                        new Dialogue(player) {
                            @Override
                            public void buildDialogue() {
                                plain("This extractor converts 1 tainted essence into 1 pure extract.")
                                        .executeAction(() -> WorldTasksManager.schedule(getDelayTask(player), 0));
                            }
                        }
                );
                break;
            case "Inspect":
                player.getDialogueManager().start(
                        new Dialogue(player) {
                            @Override
                            public void buildDialogue() {
                                plain("Next to the Ventriculus, you find some scribbled notes. Presumably written by Hagus.");
                                item(WARPED_EXTRACT_PLACEHOLDER, "Fundamental. Seems to enhance the creation of the elemental runes: air, water, earth, and fire. Interestingly also mind and body runes. Cost: 1,250 coins.");
                                item(TWISTED_EXTRACT_PLACEHOLDER, "Stronger, but strangely formed. Implies chaos, cosmic and combination runes. Cost: 6,000 coins.");
                                item(MANGLED_EXTRACT_PLACEHOLDER, "Calculations imply use for nature, law, astral and death runes. Cost: 12,000 coins.");
                                item(SCARRED_EXTRACT_PLACEHOLDER, "Extremely powerful. Save for the most precious runes. Blood, soul... And dare I hope... Wrath. Cost: 24,000 coins.");
                                }
                        });
        }
    }

    private TickTask getDelayTask(Player player) {
        return new TickTask() {
            @Override
            public void run() {
                player.getDialogueManager().start(new ExtractOrificeActionD(player));
                stop();
            }
        };
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.EXTRACT_ORIFICE};
    }

    @Override
    public int getDelay() {
        return ObjectAction.super.getDelay();
    }
}
