package org.jesse.game.content.skills.agility.draynorrooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class DraynorRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double {
        return 0.0
    }

    companion object {
        @JvmField
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(
            Location(3101, 3278, 3), Location(3091, 3275, 3),
            Location(3093, 3266, 3), Location(3098, 3259, 3)
        )
    }
}
