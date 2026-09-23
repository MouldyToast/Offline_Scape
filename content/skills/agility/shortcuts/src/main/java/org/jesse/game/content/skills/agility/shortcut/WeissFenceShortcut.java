package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

public class WeissFenceShortcut implements Shortcut {

    private static final Animation SQUEEZE = new Animation(1237);
    private static final SoundEffect SQUEEZE_SOUND = new SoundEffect(2489);

    private static final Location WEST = new Location(2865, 3938, 0);
    private static final Location EAST = new Location(2867, 3938, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean fromWest = player.getX() <= 2865;
        final Location destination = fromWest ? EAST : WEST;
        player.faceObject(object);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(SQUEEZE);
                    player.setForceMovement(new ForceMovement(destination, 76,
                            fromWest ? ForceMovement.EAST : ForceMovement.WEST));
                    player.sendSound(SQUEEZE_SOUND);
                } else if (ticks == 2) {
                    player.setLocation(destination);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public String getFilterableStartMessage(final boolean success) {
        return success ? "You squeeze through the fence." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{46815};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}