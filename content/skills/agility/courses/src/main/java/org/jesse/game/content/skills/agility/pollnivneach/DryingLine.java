package org.jesse.game.content.skills.agility.pollnivneach;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/30/2020
 */
public final class DryingLine extends AgilityCourseObstacle {
    private static final ImmutableLocation START = new ImmutableLocation(3362, 3002, 2);
    private static final ImmutableLocation DRYING_LINE = new ImmutableLocation(3363, 3000, 2);
    private static final ImmutableLocation FINISH = new ImmutableLocation(3363, 2998, 0);
    private static final Animation jumpToLineAnim = new Animation(741);
    private static final Animation jumpToFinishAnim = new Animation(2586);
    private static final ForceMovement jumpToLineMovement = new ForceMovement(DRYING_LINE, 30, ForceMovement.SOUTH);
    private static final SoundEffect jumpToLineSound = new SoundEffect(2461);
    private static final SoundEffect jumpToFinishSound = new SoundEffect(2462, 0, 15);

    public DryingLine() {
        super(PollnivneachRooftopCourse.class, 9);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setLocation(START.transform(0, 0, 1));
        player.setAnimation(jumpToLineAnim);
        player.setForceMovement(jumpToLineMovement);
        player.sendSound(jumpToLineSound);
        WorldTasksManager.schedule(() -> player.setLocation(DRYING_LINE));
        WorldTasksManager.schedule(() -> {
            player.setAnimation(jumpToFinishAnim);
            player.sendSound(jumpToFinishSound);
        }, 1);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(PollnivneachRooftopCourse.landAnim);
            player.setLocation(FINISH);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        }, 2);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.DRYING_LINE};
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 540;
    }
}
