package org.jesse.game.content.minigame.barrows;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 30/11/2018 16:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsOverlayInterface extends Interface {
    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        final Barrows barrows = player.getBarrows();
        if (barrows.isLooted()) {
            return;
        }
        player.getInterfaceHandler().sendInterface(getInterface());
        barrows.refreshInterface();
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BARROWS_OVERLAY;
    }
}
