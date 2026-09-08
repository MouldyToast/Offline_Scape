package org.jesse.plugins.dialogue;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-1-2019 | 22:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface ItemDialogue {

    void run(final Item item);

    default void execute(final Player player, final int itemId) {
        if (itemId < 0) {
            return;
        }
        if (player.getTemporaryAttributes().get("interfaceInput") != this) {
            return;
        }
        run(new Item(itemId));
        //The block inside run can change the attribute, so we re-check it.
        if (player.getTemporaryAttributes().get("interfaceInput") != this) {
            return;
        }
        player.getTemporaryAttributes().remove("interfaceInput");
        player.getTemporaryAttributes().remove("interfaceInputNoCloseOnButton");
    }
}
