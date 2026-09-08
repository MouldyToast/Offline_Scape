package org.jesse.game.content.skills.magic.lecterns;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 03/09/2019 08:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HomeLecternObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        GameInterface.REGULAR_LECTERN.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LECTERN_35008 };
    }
}
