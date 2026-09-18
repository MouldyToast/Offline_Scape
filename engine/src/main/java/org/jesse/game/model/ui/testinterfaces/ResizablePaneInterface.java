package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.gravestone.GravestoneExt;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;

/**
 * @author Kris | 07/01/2019 15:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ResizablePaneInterface extends Interface {

    @Override
    protected void attach() {
        put(21, "Gravestone info");
        put(43, "Open Game Noticeboard");
        put(60, "Character Summary");
        put(64, "Toggle prayer filtering");
        put(65, "Toggle spell filtering");
    }

    @Override
    public void open(Player player) {
        throw new IllegalStateException("Panes cannot be opened as interfaces.");
    }

    @Override
    protected void build() {
        bind("Toggle spell filtering", (player, slotId, itemId, option) -> {
            if (option == 2) {
                player.getSettings().toggleSetting(Setting.SPELL_FILTERING_DISABLED);
            } else if (option == 3) {
                player.getTeleportsManager().attemptTeleport(player.getTeleportsManager().getPreviousDestination());
            }
        });
        bind("Toggle prayer filtering", (player, slotId, itemId, option) -> {
            if (option == 2) {
                player.getSettings().toggleSetting(Setting.PRAYER_FILTER_DISABLE);
            }
        });
        bind("Gravestone info", GravestoneExt.INSTANCE::informGravestone);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RESIZABLE_PANE;
    }
}
