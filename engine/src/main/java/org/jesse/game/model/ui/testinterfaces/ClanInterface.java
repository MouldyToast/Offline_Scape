package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

import static org.jesse.game.model.ui.chat_channel.ChatChannelPlayerExtKt.getChatChannelInterfaceType;

/**
 * @author Tommeh | 27-10-2018 | 19:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClanInterface extends Interface {

    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        getChatChannelInterfaceType(player).sendTabInterface(player, getInterface());
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.YOUR_CLAN_TAB;
    }
}
