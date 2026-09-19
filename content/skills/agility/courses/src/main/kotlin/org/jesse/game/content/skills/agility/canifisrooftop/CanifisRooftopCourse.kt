package org.jesse.game.content.skills.agility.canifisrooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class CanifisRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double {
        return 0.0
    }

    companion object {
        @JvmField
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(3499, 3505, 2), Location(3488, 3500, 2),
            Location(3476, 3494, 3), Location(3478, 3483, 2),
            Location(3497, 3471, 3), Location(3514, 3478, 2),
        )
    }
}
