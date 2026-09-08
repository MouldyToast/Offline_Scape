package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.achievementdiary.diaries.KourendDiary;
import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 17:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkAltarBoulder implements Shortcut {

    private static final Animation CLIMB = new Animation(839);
    private static final Location NORTH = new Location(1776, 3884, 0);
    private static final Location SOUTH = new Location(1776, 3882, 0);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return player.getY() > object.getY() ? object : new Location(1776, 3880, 0);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean north = player.getY() > object.getY();
        if (north) {
            WorldTasksManager.schedule(new WorldTask() {
                private int ticks;
                @Override
                public void run() {
                    if(ticks == 0) {
                        player.setAnimation(CLIMB);
                        player.setForceMovement(new ForceMovement(SOUTH, 60, ForceMovement.SOUTH));
                    } else if(ticks == 2) {
                        player.setLocation(SOUTH);
                        player.sendMessage("You climb over the rocks.");
                    } else if (ticks == 3) {
                        player.addWalkSteps(1776, 3880, -1, false);
                        player.getAchievementDiaries().update(KourendDiary.USE_BOULDER_LEAP);
                        stop();
                    }
                    ticks++;
                }

            }, 0, 0);
        } else {
            player.addWalkSteps(SOUTH.getX(), SOUTH.getY(), -1, false);
            WorldTasksManager.schedule(new WorldTask() {

                private int ticks;

                @Override
                public void run() {
                    if(ticks == 0) {
                        player.setAnimation(CLIMB);
                        player.setForceMovement(new ForceMovement(NORTH, 60, ForceMovement.NORTH));
                    } else if(ticks == 2) {
                        player.setLocation(NORTH);
                        player.sendMessage("You climb over the rocks.");
                        stop();
                    }
                    ticks++;
                }

            }, 2, 0);
        }
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 49;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 27990 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 5;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
