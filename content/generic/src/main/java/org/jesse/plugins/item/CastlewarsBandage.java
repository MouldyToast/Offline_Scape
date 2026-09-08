package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastlewarsBandage extends ItemPlugin {

    @Override
    public void handle() {
        bind("Heal", new OptionHandler() {
            @Override
            public void handle(final Player player, final Item item, final Container container, final int slotId) {
                if(!player.inArea("Castle Wars")) {
                    player.getInventory().deleteItem(item.getId(), player.getInventory().getAmountOf(item.getId()));
                    player.sendMessage("You cannot have or use these outside of Castle Wars!");
                    return;
                }

                final boolean cwarsBracelet = player.getTemporaryAttributes().containsKey("castle wars bracelet effect");
                int amt = (int) (Math.floor(player.getMaxHitpoints() * 0.1) * (cwarsBracelet ? 1.5 : 1));

                player.getInventory().deleteItem(slotId, item);
                player.heal(amt);
                player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 30.0);
                player.sendMessage("You heal yourself with the bandages.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 4049 };
    }
}
