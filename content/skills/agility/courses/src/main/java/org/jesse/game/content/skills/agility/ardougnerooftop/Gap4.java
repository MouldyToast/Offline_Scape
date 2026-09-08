package org.jesse.game.content.skills.agility.ardougnerooftop;

import org.jesse.game.content.achievementdiary.diaries.ArdougneDiary;
import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 08/06/2019 | 12:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Gap4 extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(2586, 15);
    private static final Animation ANIM2 = new Animation(2588);
    private static final Animation ANIM3 = new Animation(741);
    private static final Animation ANIM4 = new Animation(2586);

    private static final Location LOCATION1 = new Location(2658, 3298, 1);
    private static final Location LOCATION2 = new Location(2668, 3297, 0);

    private static final Location LOCATION5 = new Location(2663, 3297, 1);
    private static final Location LOCATION6 = new Location(2667, 3297, 1);

    private static final SoundEffect EFFECT1 = new SoundEffect(2462, 0, 15);

    public Gap4() {
        super(ArdougneRooftopCourse.class, 7);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 90;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.GAP_15612 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 17;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.setFaceLocation(object);
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                    case 0:
                        player.setAnimation(ANIM1);
                        player.sendSound(EFFECT1);
                        break;
                    case 1:
                        player.setAnimation(ANIM2);
                        player.setLocation(LOCATION1);
                        break;
                    case 3:
                        player.addWalkSteps(2661, 3298, -1, false);
                        break;
                    case 7:
                        player.setAnimation(ANIM3);
                        player.autoForceMovement(LOCATION5, 15, 30);
                        break;
                    case 10:
                        player.addWalkSteps(2666, 3297, -1, false);
                        break;
                    case 14:
                        player.setAnimation(ANIM3);
                        player.autoForceMovement(LOCATION6, 15, 30);
                        break;
                    case 15:
                        player.setAnimation(ANIM4);
                        break;
                    case 16:
                        player.setAnimation(ANIM2);
                        player.setLocation(LOCATION2);
                        player.getAchievementDiaries().update(ArdougneDiary.COMPLETE_ARDOUGNE_ROOFTOP_LAP);
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90);
                        break;
                }

            }
        }, 1, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 529;
    }
}
