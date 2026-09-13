package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.GlobalAreaManager;

import java.util.Set;

/**
 * @author Tommeh | 02/05/2019 | 18:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GiantMoleHillObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Look-inside")) {
            final Set<Player> players = GlobalAreaManager.get("Falador Mole Lair").getPlayers();
            final int playerCount = players.size();
            player.sendMessage("You look inside the mole hill and see " + (playerCount == 0 ? "no adventurers" : playerCount == 1 ? "1 adventurer" : playerCount + " adventurers") + " inside the mole tunnels.");
        }
    }


    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MOLE_HILL };
    }
}
