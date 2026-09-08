package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ForcedGate;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Kris | 27/04/2019 01:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SophanemGate implements ObjectAction {
    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        new ForcedGate<>(player, object).handle(Optional.empty());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                44036, 44038
        };
    }
}
