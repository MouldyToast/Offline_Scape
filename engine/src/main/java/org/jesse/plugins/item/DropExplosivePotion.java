package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class DropExplosivePotion extends ItemPlugin {

    private final static ForceTalk OW = new ForceTalk("Ow!");

    @Override
    public void handle() {
        bind("Drop", new OptionHandler() {
            @Override
            public void handle(final Player player, final Item item, final Container container, final int slotId) {
                if (!player.inArea("Castle Wars")) {
                    player.getInventory().deleteItem(item.getId(), player.getInventory().getAmountOf(item.getId()));
                    return;
                }

                player.getInventory().deleteItem(slotId, item);
                player.applyHit(new Hit(15, HitType.REGULAR));
                player.setForceTalk(OW);
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 4045 };
    }
}
