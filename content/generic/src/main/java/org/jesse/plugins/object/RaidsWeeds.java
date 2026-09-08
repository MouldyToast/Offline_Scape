package org.jesse.plugins.object;

import org.jesse.game.content.chambersofxeric.skills.RaidRakingAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 3. mai 2018 : 01:43:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RaidsWeeds implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getActionManager().setAction(new RaidRakingAction());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WEEDS_29773 };
    }
}
