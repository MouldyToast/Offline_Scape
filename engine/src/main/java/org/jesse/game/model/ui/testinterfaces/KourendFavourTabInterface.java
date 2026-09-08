package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.PaneType;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 28-10-2018 | 19:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KourendFavourTabInterface extends Interface {

    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface().getId(), 43, PaneType.JOURNAL_TAB_HEADER, true);
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.KOUREND_FAVOUR_TAB;
    }
}
