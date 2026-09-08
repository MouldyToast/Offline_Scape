package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

public class LavaMazeStone implements Shortcut {
    private static final Location NORTH = new Location(3092, 3883, 0);
    private static final Location SOUTH = new Location(3092, 3878, 0);
    private static final Location STONE = new Location(3092, 3880, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (!DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player)) {
            player.sendMessage("You need to complete the wilderness hard diary to use this shortcut.");
            return;
        }
        final boolean north = player.getY() > object.getY();
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, STONE, 35, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if (ticks == 1) player.setLocation(STONE);
                 else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, north ? SOUTH : NORTH, 35, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if (ticks == 3) {
                    player.setLocation(north ? SOUTH : NORTH);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 82;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {14917};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
