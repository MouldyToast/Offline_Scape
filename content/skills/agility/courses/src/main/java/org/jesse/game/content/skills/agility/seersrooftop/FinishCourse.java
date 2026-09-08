package org.jesse.game.content.skills.agility.seersrooftop;

import org.jesse.game.content.achievementdiary.diaries.KandarinDiary;
import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public final class FinishCourse extends AgilityCourseObstacle {

    private static final Location FINISH = new Location(2704, 3464, 0);

    public FinishCourse() {
        super(SeersRooftopCourse.class, 6);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 60;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.EDGE_14931};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 2;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setFaceLocation(FINISH);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;

            @Override
            public void run() {
                if (ticks == 0)
                    player.setAnimation(Animation.LEAP);
                else if (ticks == 1) {
                    player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_LAPS_SEERS_COURSE);
                    player.getAchievementDiaries().update(KandarinDiary.COMPLETE_SEERS_VILLAGE_AGILITY_COURSE_LAP);
                    player.setAnimation(Animation.LAND);
                    player.setLocation(FINISH);
                    MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 435;
    }
}
