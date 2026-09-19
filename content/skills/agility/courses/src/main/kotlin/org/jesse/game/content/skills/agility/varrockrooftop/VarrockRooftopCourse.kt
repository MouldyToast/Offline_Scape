package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class VarrockRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double = 0.0

    companion object {
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(3215, 3410, 3), Location(3195, 3416, 1), Location(3193, 3395, 3),
            Location(3222, 3402, 3), Location(3237, 3406, 3)
        )
    }
}
