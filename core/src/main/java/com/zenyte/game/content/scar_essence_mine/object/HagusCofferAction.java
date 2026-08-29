package com.zenyte.game.content.scar_essence_mine.object;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

import static com.near_reality.game.world.entity.player.PlayerAttributesKt.getScarEssenceMineCoffer;
import static com.zenyte.game.item.ItemId.COINS_995;

/**
 * @author Zei
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/18/2025
 */
public class HagusCofferAction implements ObjectAction {

    private static final Item COINS_PLACEHOLDER = new Item(1000, 1);

    private int getCofferAmount(Player player) {
        return PlayerAttributesKt.getScarEssenceMineCoffer(player);
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
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Use")) {
            player.getDialogueManager().start(
                    new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            var hagusCoffer = getScarEssenceMineCoffer(player);
                            options(Colour.RS_RED.wrap("Current coffer: " + Utils.format(hagusCoffer)), "Deposit", "Withdraw")
                                    .onOptionOne(() ->
                                        WorldTasksManager.schedule(getDepositTask(player)))
                                    .onOptionTwo(() ->
                                        WorldTasksManager.schedule(getWithdrawTask(player)));
                        }
                    }
            );
        ;}
    }

    private TickTask getDepositTask(Player player) {
        return new TickTask() {
            @Override
            public void run() {
                var currentCoinsInInv = player.getInventory().getAmountOf(COINS_995);
                player.sendInputInt("How many coins would you like to add (0-" + currentCoinsInInv + ")?", amount -> {
                    if (amount <= 0) {
                        player.getDialogueManager().start(new PlainChat(player, "Invalid amount entered."));
                        return;
                    }
                    if (amount > player.getInventory().getAmountOf(995)) {
                        player.getDialogueManager().start(new PlainChat(player, "You don't have that many coins."));
                        return;
                    }
                    if (getCofferAmount(player) == Integer.MAX_VALUE) {
                        player.getDialogueManager().start(new PlainChat(player, "Your coffer cannot hold any more coins."));
                        return;
                    }
                    var currentCoffer = getScarEssenceMineCoffer(player);
                    var newCofferAmount = Math.min(Integer.MAX_VALUE, currentCoffer + amount);
                    var amountToDebit = newCofferAmount - currentCoffer;

                    if (player.getInventory().deleteItem(new Item(COINS_995, amountToDebit)).getResult() == RequestResult.SUCCESS) {
                        PlayerAttributesKt.setScarEssenceMineCoffer(player, newCofferAmount);
                        player.getDialogueManager().start(new ItemChat(player, COINS_PLACEHOLDER,"You added " + Utils.format(amount) + " coins to your coffer. There are now " + Utils.format(newCofferAmount) + " coins in it."));
                    } else {
                        player.getDialogueManager().start(new PlainChat(player, "An error occurred. Please try again."));
                    }
                });
                stop();

            }
        };
    }
    private TickTask getWithdrawTask(Player player) {
        return new TickTask() {
            @Override
            public void run() {
                var currentCoffer = getCofferAmount(player);
                    if (getCofferAmount(player) == 0) {
                        player.getDialogueManager().start(new ItemChat(player, COINS_PLACEHOLDER, "You have no coins to withdraw from the coffer."));
                        return;
                    }
                    player.sendInputInt("How many coins would you like to withdraw? (0-" + currentCoffer + ")? ", amount -> {
                        if (amount <= 0) {
                            player.getDialogueManager().start(new PlainChat(player, "Invalid amount entered."));
                            return;
                        }
                        if (amount > currentCoffer) {
                            player.getDialogueManager().start(new PlainChat(player, "You don't have enough coins in the coffer."));
                            return;
                        }
                        if (player.getInventory().addItem(new Item(COINS_995, amount)).getResult() == RequestResult.SUCCESS) {
                            PlayerAttributesKt.setScarEssenceMineCoffer(player, currentCoffer - amount);
                            var newCurrentCoffer = getCofferAmount(player);
                            player.getDialogueManager().start(new ItemChat(player, COINS_PLACEHOLDER,"You withdrew " + Utils.format(amount) + " coins from your coffer. There are now " + Utils.format(newCurrentCoffer) + " coins in it."));
                            //player.sendMessage("The coffer contains " + Colour.RS_RED.wrap(Utils.format(newCurrentCoffer)) + " coins.");
                        } else {
                            player.getDialogueManager().start(new PlainChat(player, "An error occurred. Please try again."));
                        }
                    });
                stop();
            }
        };
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.COFFER_49921};
    }
}
