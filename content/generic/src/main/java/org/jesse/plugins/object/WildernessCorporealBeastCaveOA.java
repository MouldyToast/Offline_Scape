package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.variables.TickVariable;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10. veebr 2018 : 3:07.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WildernessCorporealBeastCaveOA implements ObjectAction {

    private static final Location TILE = new Location(2964, 4382, 2);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getVariables().getTime(TickVariable.TELEBLOCK) > 0) {
            player.sendMessage("You cannot enter the cave while teleblocked.");
            return;
        }
        if (player.isUnderCombat()) {
            player.sendMessage("You cannot enter the cave while in combat.");
            return;
        }
        player.lock(2);
        player.setLocation(TILE);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE };
    }
}
