package org.jesse.game.content.skills.agility.pyramid

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.entity.player.VarManager
import org.jesse.game.world.`object`.WorldObject
import org.jesse.utils.StaticInitializer
import kotlin.math.min

@StaticInitializer
class AgilityPyramid : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double = 0.0

    override fun runCourseObstacle(player: Player, `object`: WorldObject?, obstacle: AgilityCourseObstacle) {
        obstacle.updateStage(player)

        val completionExp = min(
            MAX_COMPLETION_BONUS,
            (300 + player.getSkills().getLevelForXp(SkillConstants.AGILITY) * 8).toDouble()
        )
        obstacle.handle(player, `object`, if (obstacle is Doorway) completionExp else 0.0, null)
    }

    companion object {
        const val MAX_COMPLETION_BONUS: Double = 1000.0
        const val HIDE_PYRAMID_VARBIT: Int = 1556
        const val MOVING_BLOCK_VARBIT: Int = 1550

        init {
            VarManager.appendPersistentVarbit(HIDE_PYRAMID_VARBIT)
            VarManager.appendPersistentVarbit(MOVING_BLOCK_VARBIT)
        }
    }
}
