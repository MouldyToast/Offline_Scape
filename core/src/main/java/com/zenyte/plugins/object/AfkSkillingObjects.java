package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.afk.AFKSkillingObject;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.logger.NearRealityPrintStream;

import java.util.ArrayList;

/**
 * @author Kris | 10. nov 2017 : 22:20.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class AfkSkillingObjects implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        AFKSkillingObject afk = AFKSkillingObject.forId(object.getId());
        if (afk == null)
            return;
        try {
            player.getActionManager().setAction(afk.getClazz().getDeclaredConstructor().newInstance());
        }catch (Exception e) {
            e.printStackTrace(NearRealityPrintStream.getErrorStream());
        }
    }

    @Override
    public Object[] getObjects() {
        final ArrayList<Object> list = new ArrayList<Object>();
        for (final AFKSkillingObject r : AFKSkillingObject.VALUES) {
            if (r.getObjectId() == -1) {
                continue;
            }
            list.add(r.getObjectId());
        }
        return list.toArray(new Object[list.size()]);
    }
}
