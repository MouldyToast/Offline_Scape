package org.jesse.game.content.skills.agility.seersrooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class SeersRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double = 0.0

    companion object {
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(2725, 3494, 3), Location(2708, 3492, 2), Location(2712, 3478, 2),
            Location(2702, 3473, 3), Location(2699, 3462, 2)
        )
    }
}
