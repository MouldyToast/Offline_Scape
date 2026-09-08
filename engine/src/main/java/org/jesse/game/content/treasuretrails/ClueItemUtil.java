package org.jesse.game.content.treasuretrails;

import org.jesse.game.item.Item;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ClueItemUtil {
    public static final void roll(@NotNull final Player player, final int roll, final int level, @NotNull final Function<ClueItem, Integer> fun) {
        if (roll < 0) {
            return;
        }
        final int rate = (int) (1.0 / ((100.0 + level) / (double) roll));
        if (Utils.random(rate - 1) == 0) {
            int typeRand = Utils.random(13);
            for (final ClueItem item : ClueItem.values()) {
                if ((typeRand -= item.getSkillingChance()) < 0) {
                    if (item == ClueItem.MASTER) {
                        throw new IllegalStateException();
                    }
                    final Integer itemId = fun.apply(item);
                    if (itemId == -1) {
                        throw new IllegalStateException();
                    }
                    final Item skillingItem = new Item(itemId);
                    player.getInventory().addOrDrop(skillingItem);
                    final String name = skillingItem.getName().toLowerCase();
                    final String itemTypeName = name.substring(0, name.indexOf(" ("));
                    final String clueTypeName = name.substring(name.indexOf(" (") + 2, name.length() - 1);
                    final String prefix = Utils.getAOrAn(clueTypeName);
                    player.sendMessage(Colour.RED.wrap("You find " + prefix + " " + clueTypeName + " " + itemTypeName + "!"));
                    return;
                }
            }
            throw new IllegalStateException();
        }
    }
}
