package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation

class PollnivneachRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double = 0.0

    companion object {
        val runningStartAnim: Animation = Animation(1995)
        val landAnim: Animation = Animation(2588)
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(3346, 2968, 1), Location(3354, 2974, 1),
            Location(3361, 2993, 2)
        )
    }
}
