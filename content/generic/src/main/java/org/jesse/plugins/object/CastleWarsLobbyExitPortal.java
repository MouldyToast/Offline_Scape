package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

import static org.jesse.game.content.minigame.castlewars.CastleWarsLobby.MAIN_LOBBY_SPAWN;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLobbyExitPortal implements ObjectAction {

    private static final int ZAMORAK_PORTAL = 4390;
    private static final int SARADOMIN_PORTAL = 4389;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if(!player.inArea("Castle Wars Lobby")) {
            return;
        }

        player.setLocation(MAIN_LOBBY_SPAWN);
        return;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { SARADOMIN_PORTAL, ZAMORAK_PORTAL };
    }
}
