package org.jesse.game.content.treasuretrails.stash;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 29/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class STASHNoticeboard implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.STASH_UNIT.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.NOTICEBOARD_29718 };
    }
}
