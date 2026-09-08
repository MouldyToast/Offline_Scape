package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * @author Kris | 29/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BugLantern extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item tinderbox = from.getId() == ItemId.TINDERBOX ? from : to;
        final Item lightSource = tinderbox == from ? to : from;
        if (player.getSkills().getLevel(SkillConstants.FIREMAKING) < 33) {
            player.sendMessage("You need a level of at least 33 Firemaking to light this.");
            return;
        }
        player.getInventory().set(lightSource == from ? fromSlot : toSlot, new Item(ItemId.LIT_BUG_LANTERN));
        player.getPacketDispatcher().sendSoundEffect(LightSourceItem.LightSource.LIGHT_SOUND);
        player.sendMessage("You light the " + lightSource.getName().toLowerCase() + ".");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {new ItemPair(ItemId.TINDERBOX, ItemId.UNLIT_BUG_LANTERN)};
    }

    @Override
    public void handle() {
        bind("Extinguish", (player, item, container, slotId) -> {
            player.sendMessage("You extinguish the " + item.getName().toLowerCase() + ".");
            player.getInventory().set(slotId, new Item(ItemId.UNLIT_BUG_LANTERN));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.LIT_BUG_LANTERN, ItemId.UNLIT_BUG_LANTERN};
    }
}
