package org.jesse.game.content.skills.agility.alkharidrooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class AlKharidRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double {
        return 0.0
    }

    companion object {
        @JvmField
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(3275, 3186, 3), Location(3267, 3170, 3),
            Location(3290, 3162, 3), Location(3317, 3161, 1),
            Location(3317, 3177, 2), Location(3303, 3189, 3)
        )
    }
}
