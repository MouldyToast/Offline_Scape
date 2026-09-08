package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;

/**
 * @author Tommeh | 28-10-2018 | 15:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FriendsListInterface extends Interface {

    @Override
    protected void attach() {
        put(1, "View Ignore List");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("View Ignore List", player -> player.getSettings().toggleSetting(Setting.FRIEND_LIST_TOGGLE));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.FRIEND_LIST_TAB;
    }
}
