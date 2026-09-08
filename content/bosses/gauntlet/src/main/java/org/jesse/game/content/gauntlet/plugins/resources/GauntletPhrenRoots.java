package org.jesse.game.content.gauntlet.plugins.resources;

import org.jesse.game.content.gauntlet.actions.PhrenRootsAction;
import org.jesse.game.content.gauntlet.objects.PhrenRoots;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public final class GauntletPhrenRoots implements ObjectAction {

    private static final int PHREN_ROOTS = 36066;

    private static final int PHREN_ROOTS_CORRUPTED = 35969;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!(object instanceof PhrenRoots)) {
            return;
        }
        final PhrenRoots roots = (PhrenRoots) object;

        boolean corrupted = object.getId() == PHREN_ROOTS_CORRUPTED;
        player.getActionManager().setAction(new PhrenRootsAction(roots, corrupted));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { PHREN_ROOTS, PHREN_ROOTS_CORRUPTED };
    }

}
