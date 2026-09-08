package org.jesse.game.content.skills.agility.pyramid;

import org.jesse.game.content.skills.agility.AbstractAgilityCourse;
import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.VarManager;
import org.jesse.game.world.object.WorldObject;
import org.jesse.utils.StaticInitializer;

@StaticInitializer
public final class AgilityPyramid extends AbstractAgilityCourse {

    public static final double MAX_COMPLETION_BONUS = 1000;
    public static final int HIDE_PYRAMID_VARBIT = 1556;
    public static final int MOVING_BLOCK_VARBIT = 1550;

    static {
        VarManager.appendPersistentVarbit(HIDE_PYRAMID_VARBIT);
        VarManager.appendPersistentVarbit(MOVING_BLOCK_VARBIT);
    }

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

    @Override
    public void runCourseObstacle(Player player, WorldObject object, AgilityCourseObstacle obstacle) {
        obstacle.updateStage(player);

        final double completionExp = Math.min(AgilityPyramid.MAX_COMPLETION_BONUS,
                300 + player.getSkills().getLevelForXp(SkillConstants.AGILITY) * 8);
        obstacle.handle(player, object, (obstacle instanceof Doorway) ? completionExp : 0, null);
    }

}
