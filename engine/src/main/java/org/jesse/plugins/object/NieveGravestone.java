package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 01/10/2019 | 21:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class NieveGravestone implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Read")) {
            GameInterface.NIEVE_GRAVESTONE.open(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 28722 };
    }
}
