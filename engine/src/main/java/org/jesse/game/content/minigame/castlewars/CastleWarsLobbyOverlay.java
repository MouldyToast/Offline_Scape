package org.jesse.game.content.minigame.castlewars;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLobbyOverlay extends Interface {

    public static final int TIMER_VARP = 380;

    @Override
    protected void attach() {}

    @Override
    protected void build() {}

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.CASTLE_WARS_LOBBY_OVERLAY;
    }
}
