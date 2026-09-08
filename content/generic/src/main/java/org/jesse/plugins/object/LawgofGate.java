package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ForcedGate;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import java.util.Optional;

public class LawgofGate implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        new ForcedGate<>(player, object).handle(Optional.empty());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_15604, ObjectId.GATE_15605 };
    }
}
