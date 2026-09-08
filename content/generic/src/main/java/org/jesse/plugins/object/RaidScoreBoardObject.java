package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 17. mai 2018 : 19:52:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidScoreBoardObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        GameInterface.RAIDS_SCOREBOARD.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SCORE_BOARD_32544 };
    }
}
