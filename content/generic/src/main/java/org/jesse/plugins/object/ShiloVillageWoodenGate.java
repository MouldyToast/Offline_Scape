package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ForcedLongGate;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Kris | 27/03/2019 20:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShiloVillageWoodenGate implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        new ForcedLongGate<>(player, object).handle(Optional.empty());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WOODEN_GATE, ObjectId.WOODEN_GATE_2262 };
    }
}
