package org.jesse.game.content.gauntlet.plugins;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public final class GauntletLobbyPortal implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

    }

    @Override
    public Object[] getObjects() {
        return new Object[0];
    }

}
