package org.jesse.game.content.scar_essence_mine.object;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;
import org.jesse.plugins.dialogue.PlainChat;

import static org.jesse.game.item.ids.ItemId.COINS_995;

/**
 * @author Zei
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/18/2025
 */
public class CoinsOnHagusCoffer implements ItemOnObjectAction {

    private static final Item COINS_PLACEHOLDER = new Item(1000, 1);

    private int getCofferAmount(Player player) {
        return PlayerAttributesKt.getScarEssenceMineCoffer(player);
    }
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (player.getInventory().getAmountOf(995) == 0) {
            player.getDialogueManager().start(new ItemChat(player, COINS_PLACEHOLDER, "You have no coins to add to the coffer."));
            return;
        }
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

            var currentCoffer = getCofferAmount(player);
            var newCofferAmount = Math.min(Integer.MAX_VALUE, currentCoffer + amount);
            var amountToDebit = newCofferAmount - currentCoffer;

            if (player.getInventory().deleteItem(new Item(COINS_995, amountToDebit)).getResult() == RequestResult.SUCCESS) {
                PlayerAttributesKt.setScarEssenceMineCoffer(player, newCofferAmount);
                player.getDialogueManager().start(new ItemChat(player, COINS_PLACEHOLDER,"You added " + Utils.format(amount) + " coins to your coffer. There are now " + Utils.format(newCofferAmount) + " coins in it."));
            }
        });
    }

    @Override
    public int getObjectStrategyDistance(WorldObject obj) {
        if (obj.getId() == ObjectId.COFFER_49921) return 0;
        return ItemOnObjectAction.super.getObjectStrategyDistance(obj);
    }
    @Override
    public Object[] getItems() {
        return new Object[]{COINS_995};
    }
    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.COFFER_49921};
    }
}
