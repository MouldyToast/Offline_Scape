package org.jesse.game.content.skills.agility.barbariancourse

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import java.util.function.Consumer

class BarbarianOutpostCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double {
        return 46.2
    }

    override fun runCourseObstacle(player: Player, `object`: WorldObject, obstacle: AgilityCourseObstacle) {
        obstacle.updateStage(player)

        val onComplete: Consumer<Player?>?
        val additionalXP: Double
        val index: Int = obstacle.getIndex()
        if (index == getObstacleCount() && (`object`.x == 2542 && `object`.id == 1948)) {
            additionalXP = getAdditionalCompletionXP()
            onComplete = onComplete()
        } else {
            additionalXP = 0.0
            onComplete = null
        }

        obstacle.handle(player, `object`, additionalXP, onComplete)
    }
}
