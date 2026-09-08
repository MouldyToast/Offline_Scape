package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.room.VespulaRoom;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.utils.TimeUnit;

/**
 * @author Kris | 12/09/2019 15:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SepticTendrils implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            raid.ifInRoom(object, VespulaRoom.class, room -> {
                if (room.isFinished()) {
                    player.sendMessage("The tendril is about to collapse!");
                    return;
                }
                if (player.getNumericTemporaryAttribute("tendril_cox_delay").longValue() > System.currentTimeMillis()) {
                    return;
                }
                player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(15));
                player.lock();
                player.resetWalkSteps();
                player.setRunSilent(true);
                if (object.getX() > player.getX()) {
                    player.addWalkSteps(player.getX() + 2, player.getY(), 2, false);
                } else if (object.getX() < player.getX()) {
                    player.addWalkSteps(player.getX() - 2, player.getY(), 2, false);
                } else if (object.getY() > player.getY()) {
                    player.addWalkSteps(player.getX(), player.getY() + 2, 2, false);
                } else {
                    player.addWalkSteps(player.getX(), player.getY() - 2, 2, false);
                }
                player.applyHit(new Hit((int) (player.getMaxHitpoints() * 0.15F), HitType.POISON));
                WorldTasksManager.schedule(() -> {
                    player.setRunSilent(false);
                    player.unlock();
                    player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(2));
                }, 1);
            });
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SEPTIC_TENDRILS };
    }
}
