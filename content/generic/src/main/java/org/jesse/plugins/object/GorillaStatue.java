package org.jesse.plugins.object;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import static org.jesse.plugins.object.AltarOPlugin.PRAY_ANIM;

/**
 * @author Kris | 26/04/2019 14:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GorillaStatue implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Pray-at")) {
            if (player.getPrayerManager().getPrayerPoints() >= player.getSkills().getLevelForXp(SkillConstants.PRAYER)) {
                player.sendMessage("You already have full prayer points.");
                return;
            }
            player.lock();
            player.sendMessage("You pray to the gods...");
            player.sendSound(2674);
            player.setAnimation(PRAY_ANIM);
            WorldTasksManager.schedule(() -> {
                player.getPrayerManager().restorePrayerPoints(99);
                player.sendMessage("... and recharge your prayer.");
                player.unlock();
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GORILLA_STATUE_4859 };
    }
}
