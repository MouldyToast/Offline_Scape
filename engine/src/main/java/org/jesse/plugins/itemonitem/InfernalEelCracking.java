package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 08/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InfernalEelCracking implements PairedItemOnItemPlugin {
    private static final int ONYX_CHANCE = 16;
    private static final int LAVA_SCALE_SHARD_CHANCE = 12;
    private static final Animation animation = new Animation(5754);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.getActionManager().setAction(new Action() {
            @Override
            public boolean initiateOnPacketReceive() {
                return true;
            }
            private int ticks;
            @Override
            public boolean start() {
                return true;
            }
            @Override
            public boolean process() {
                if (!player.getInventory().containsItem(ItemId.INFERNAL_EEL, 1)) {
                    return false;
                }
                if (ticks++ % 2 == 0) {
                    player.setAnimation(animation);
                }
                return true;
            }
            @Override
            public int processWithDelay() {
                final int random = Utils.random(ONYX_CHANCE * LAVA_SCALE_SHARD_CHANCE - 1);
                final Inventory inventory = player.getInventory();
                inventory.deleteItem(new Item(ItemId.INFERNAL_EEL));
                final Item item = random < ONYX_CHANCE ? new Item(ItemId.ONYX_BOLT_TIPS) : random < (ONYX_CHANCE + LAVA_SCALE_SHARD_CHANCE) ? new Item(ItemId.LAVA_SCALE_SHARD, Utils.random(1, 5)) : new Item(ItemId.TOKKUL, Utils.random(15, 30));
                inventory.addItem(item);
                player.sendFilteredMessage("You crack the infernal eel and find " + item.getAmount() + " x " + item.getName() + ".");
                return 1;
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(ItemId.HAMMER, ItemId.INFERNAL_EEL)};
    }
}
