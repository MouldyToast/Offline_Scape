package org.jesse.plugins.object;

import org.jesse.game.content.chambersofxeric.party.RaidingPartiesInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 15. nov 2017 : 21:23.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RecruitingBoardObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        RaidingPartiesInterface.refresh(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.RECRUITING_BOARD };
    }
}
