package org.jesse.plugins.object;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 1 jan. 2018 : 16:14:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class GodwarsBoulderObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getSkills().getLevel(SkillConstants.STRENGTH) < 60) {
            player.sendMessage("You need a Strength level of at least 60 to move this boulder.");
            return;
        }
        if (player.getY() <= 3715) {
            player.lock(15);
            player.addWalkSteps(2898, 3715);
            player.setAnimation(new Animation(6983));
            WorldTasksManager.schedule(new WorldTask() {

                int ticks;

                @Override
                public void run() {
                    switch(ticks++) {
                        case 1:
                            World.sendObjectAnimation(object, new Animation(6985));
                            player.setForceMovement(new ForceMovement(new Location(2898, 3719, 0), 330, ForceMovement.NORTH));
                            break;
                        case 6:
                            World.sendObjectAnimation(object, new Animation(6986));
                            player.setLocation(new Location(2898, 3719, 0));
                            player.getVariables().setRunEnergy(0);
                            object.setLocked(false);
                            player.lock(1);
                            stop();
                            break;
                    }
                }
            }, 0, 1);
        } else if (player.getY() >= 3719) {
            player.lock(15);
            player.addWalkSteps(2898, 3719);
            player.setAnimation(new Animation(6984));
            WorldTasksManager.schedule(new WorldTask() {

                int ticks;

                @Override
                public void run() {
                    switch(ticks++) {
                        case 1:
                            World.sendObjectAnimation(object, new Animation(6985));
                            player.setForceMovement(new ForceMovement(new Location(2898, 3715, 0), 330, ForceMovement.SOUTH));
                            break;
                        case 6:
                            World.sendObjectAnimation(object, new Animation(6986));
                            player.setLocation(new Location(2898, 3715, 0));
                            object.setLocked(false);
                            player.lock(1);
                            stop();
                            break;
                    }
                }
            }, 0, 1);
        }
        object.setLocked(true);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BOULDER_26415 };
    }
}
