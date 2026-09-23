package org.jesse.game.content.chompy.plugins;

import org.jesse.game.content.chompy.Chompy;
import org.jesse.game.content.chompy.ChompyBirdHat;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/6/2020
 */
public class BowItemAction extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> check(player));
        bind("Check kills", (player, item, container, slotId) -> check(player));
    }

    private void check(final Player player) {
        final int kills = player.getNumericAttribute(Chompy.KILL_ATTRIB).intValue();
        final ChompyBirdHat hat = ChompyBirdHat.getBest(kills);
        player.sendMessage("You've scratched up a total of " + kills + " chompy bird kills so far!");
        if (hat != null) {
            final String title = hat.getTitle();
            final String article = (Utils.startWithVowel(title) ? "an" : "a");
            player.sendMessage("~ You're " + article + " " + title + "! ~");
        }
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.OGRE_BOW, ItemId.COMP_OGRE_BOW};
    }
}
