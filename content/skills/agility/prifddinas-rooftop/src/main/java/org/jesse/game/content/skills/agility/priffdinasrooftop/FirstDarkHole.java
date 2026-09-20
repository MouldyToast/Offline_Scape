package org.jesse.game.content.skills.agility.priffdinasrooftop;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.object.WorldObject;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class FirstDarkHole extends AgilityCourseObstacle {

    private static final Location START_LOC = new Location(3269, 6117, 0);
    private static final Location END_LOC = new Location(3293, 6141, 0);
    private static final Animation ENTER_HOLE = new Animation(827);

    public FirstDarkHole() {
        super(PriffdinasRooftopCourse.class, 5);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{36229};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 5;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.forceAnimation(ENTER_HOLE);
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            final FadeScreen fs = new FadeScreen(player);
            @Override
            public void run() {
                switch (ticks++) {
                    case 0: {
                        fs.fade();
                        break;
                    }
                    case 2: {
                        player.teleport(END_LOC);
                        break;
                    }
                    case 5: {
                        fs.unfade();
                        player.faceDirection(Direction.NORTH);
                        stop();
                        break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 11.5;
    }
}