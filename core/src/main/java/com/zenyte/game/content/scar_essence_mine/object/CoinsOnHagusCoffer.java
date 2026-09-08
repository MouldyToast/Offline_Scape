package com.zenyte.game.content.scar_essence_mine.object;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

import static com.zenyte.game.item.ids.ItemId.COINS_995;

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
