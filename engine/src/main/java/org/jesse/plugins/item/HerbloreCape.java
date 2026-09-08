package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.SkillcapePerk;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.masks.Animation;

/**
 * @author Kris | 16/03/2019 02:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HerbloreCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Search", (player, item, slotId) -> {
            if (player.getInventory().containsItem(233, 1)) {
                player.sendMessage("You already have a pestle and mortar with you.");
                return;
            }
            player.getInventory().addOrDrop(new Item(233));
            player.sendMessage("You search the cape and find a " + Colour.RS_RED.wrap("Pestle and " +
                    "mortar."));
            player.setAnimation(new Animation(1376));
        });
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.HERBLORE.getSkillCapes();
    }
}
