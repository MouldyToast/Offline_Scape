package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.skills.RaidFishing;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 04:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BubblesObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> player.getActionManager().setAction(new RaidFishing(object)));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BUBBLES_29889 };
    }
}
