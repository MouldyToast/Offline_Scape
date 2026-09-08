package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * @author Christopher
 * @since 1/24/2020
 */
public class UnpoweredSymbolBlessingAction implements PairedItemOnItemPlugin {
    private static final Animation unholyBlessingAction = new Animation(1336);
    private static final Animation balanceBlessingAction = new Animation(1337);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (player.getSkills().checkLevel(SkillConstants.PRAYER, 50, "bless the unholy symbol.")) {
            final Item book = from.getId() != ItemId.UNPOWERED_SYMBOL ? from : to;
            player.setAnimation(book.getId() == ItemId.UNHOLY_BOOK ? unholyBlessingAction : balanceBlessingAction);
            WorldTasksManager.schedule(() -> {
                final int slot = from.getId() == ItemId.UNPOWERED_SYMBOL ? fromSlot : toSlot;
                player.getInventory().replaceItem(ItemId.UNHOLY_SYMBOL, 1, slot);
                player.getPrayerManager().drainPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.1 + 2));
                player.sendFilteredMessage("You bless the unholy symbol.");
            }, 5);
        }
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(ItemId.UNPOWERED_SYMBOL, ItemId.UNHOLY_BOOK), ItemPair.of(ItemId.UNPOWERED_SYMBOL, ItemId.BOOK_OF_BALANCE)};
    }
}
