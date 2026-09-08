package org.jesse.game.content.gauntlet.plugins.resources;

import org.jesse.game.content.gauntlet.actions.LinumTirinumAction;
import org.jesse.game.content.gauntlet.objects.LinumTirinum;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Andys1814
 */
public final class GauntletLinumTirinum implements ObjectAction {

    private static final int LINUM_TIRINUM = 36072;

    private static final int LINUM_TIRINUM_CORRUPTED = 35975;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!(object instanceof LinumTirinum)) {
            return;
        }
        final LinumTirinum linum = (LinumTirinum) object;

        boolean corrupted = object.getId() == LINUM_TIRINUM_CORRUPTED;
        player.getActionManager().setAction(new LinumTirinumAction(linum, corrupted));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { LINUM_TIRINUM, LINUM_TIRINUM_CORRUPTED };
    }

}
