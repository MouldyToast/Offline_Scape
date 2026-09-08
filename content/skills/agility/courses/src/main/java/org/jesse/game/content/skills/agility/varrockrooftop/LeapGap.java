package org.jesse.game.content.skills.agility.varrockrooftop;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public final class LeapGap extends AgilityCourseObstacle {

    private static final Animation JUMP = new Animation(2586);

    private static final Location START = new Location(3201, 3416, 3);

    private static final Location LAND = new Location(3197, 3416, 1);

    private static final Location START2 = new Location(3232, 3402, 3);

    private static final Location LAND2 = new Location(3236, 3403, 3);

    public LeapGap() {
        super(VarrockRooftopCourse.class, 3);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean direction = object.getId() == ObjectId.GAP_14414;
        player.setFaceLocation(direction ? LAND : LAND2);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP);
                } else if (ticks == 1) {
                    player.setLocation(direction ? LAND : LAND2);
                    MarkOfGrace.spawn(player, VarrockRooftopCourse.MARK_LOCATIONS, 40, 30);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        if (object.getId() == ObjectId.GAP_14835) {
            return 4;
        }
        return 17;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 30;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { ObjectId.GAP_14414, ObjectId.GAP_14835 };
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.getId() == ObjectId.GAP_14414 ? START : START2;
    }
}
