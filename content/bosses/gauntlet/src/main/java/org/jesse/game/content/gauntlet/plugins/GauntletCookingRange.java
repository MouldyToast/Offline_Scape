package org.jesse.game.content.gauntlet.plugins;

import org.jesse.game.content.gauntlet.actions.CookingRangeAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 *
 * @author Andys1814.
 * @since 1/28/2022.
 */
public final class GauntletCookingRange implements ObjectAction {

    private static final int COOKING_RANGE = 36077;

    private static final int COOKING_RANGE_CORRUPTED = 35980;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new CookingRangeAction(object.getId() == COOKING_RANGE_CORRUPTED));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { COOKING_RANGE, COOKING_RANGE_CORRUPTED };
    }

}
