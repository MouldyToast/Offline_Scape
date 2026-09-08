package org.jesse.game.content.gauntlet.plugins.resources;

import org.jesse.game.content.gauntlet.actions.GrymRootAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public final class GauntletGrymRoot implements ObjectAction {

    private static final int GRYM_ROOT = 36070;

    private static final int GRYM_ROOT_CORRUPTED = 35973;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new GrymRootAction(object, object.getId() == GRYM_ROOT_CORRUPTED));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { GRYM_ROOT, GRYM_ROOT_CORRUPTED };
    }

}
