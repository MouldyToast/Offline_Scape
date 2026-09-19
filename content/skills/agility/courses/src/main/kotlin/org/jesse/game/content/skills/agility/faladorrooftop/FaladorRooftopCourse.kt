package org.jesse.game.content.skills.agility.faladorrooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class FaladorRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double {
        return 0.0
    }

    companion object {
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(3046, 3345, 3), Location(3046, 3365, 3), Location(3036, 3363, 3),
            Location(3015, 3355, 3), Location(3011, 3339, 3), Location(3023, 3334, 3)
        )
    }
}
