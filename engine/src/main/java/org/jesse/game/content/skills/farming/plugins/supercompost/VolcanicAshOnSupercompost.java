package org.jesse.game.content.skills.farming.plugins.supercompost;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 02/24/2020
 */
public class VolcanicAshOnSupercompost implements PairedItemOnItemPlugin {
    private static final Item ultraCompost = new Item(ItemId.ULTRACOMPOST);
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        player.getDialogueManager().start(new UltracompostCreationDialogue(player, ultraCompost));
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(ItemId.SUPERCOMPOST, ItemId.VOLCANIC_ASH)
        };
    }
}
