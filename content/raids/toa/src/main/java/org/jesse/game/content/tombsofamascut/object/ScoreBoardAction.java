package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions
 */
public class ScoreBoardAction implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.TOA_SCOREBOARD.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {46071};
    }
}
