package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class AgilityPyramidClimbingRocks implements Shortcut {

    @Override
    public int getLevel(final WorldObject object) {
        if (object.getId() == ObjectId.CLIMBING_ROCKS_10852) {
            return 1;
        }
        return 30;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 11948, 11949 };
    }

    private static final Animation CLIMB_DOWN = new Animation(740);
    private static final Animation CLIMB_UP = new Animation(740);

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        if (player.getX() > object.getX()) {
            return object.transform(1, 0, 0);
        }
        return object.transform(-1, 0, 0);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean climbingDown = player.getX() <= object.getX();
        final Direction direction = climbingDown ? Direction.EAST : Direction.WEST;
        Location destination = player.getLocation().transform(direction, 4);
        int fmDirection = ForceMovement.WEST;
        ForceMovement forceMovement = new ForceMovement(destination, 120, fmDirection);
        player.setAnimation(climbingDown ? CLIMB_DOWN : CLIMB_UP);
        player.setForceMovement(forceMovement);
        player.sendSound(new SoundEffect(2454, 1, 0, 5));
        WorldTasksManager.schedule(() -> {
            player.getSkills().addXp(SkillConstants.AGILITY, climbingDown ? 5 : 1);
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 3);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
