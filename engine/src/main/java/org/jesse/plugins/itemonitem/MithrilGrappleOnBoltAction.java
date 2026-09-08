package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 20/05/2019 | 17:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class MithrilGrappleOnBoltAction implements ItemOnItemAction {
    private static final Item MITH_GRAPPLE = new Item(9418);
    private static final Item MITHRIL_BOLT = new Item(9142);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item grapple = from.getId() == 9416 ? from : to;
        player.getInventory().deleteItemsIfContains(new Item[] {grapple, MITHRIL_BOLT}, () -> {
            player.getInventory().addItem(MITH_GRAPPLE);
            player.sendMessage("You fletch a bolt.");
            player.getSkills().addXp(SkillConstants.FLETCHING, 11);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {9416, 9142};
    }
}
