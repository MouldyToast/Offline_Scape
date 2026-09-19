package org.jesse.game.content.skills.agility.rellekkarooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class RellekkaRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double = 0.0

    companion object {
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(2622, 3676, 3), Location(2617, 3664, 3), Location(2618, 3660, 3),
            Location(2628, 3652, 3), Location(2628, 3655, 3), Location(2641, 3649, 3),
            Location(2643, 3651, 3), Location(2649, 3659, 3), Location(2644, 3662, 3),
            Location(2658, 3674, 3), Location(2656, 3681, 3)
        )
    }
}
