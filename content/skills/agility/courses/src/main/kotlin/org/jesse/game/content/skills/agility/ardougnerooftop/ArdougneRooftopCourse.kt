package org.jesse.game.content.skills.agility.ardougnerooftop

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.world.entity.Location

class ArdougneRooftopCourse : AbstractAgilityCourse() {
    override fun getAdditionalCompletionXP(): Double {
        return 0.0
    }

    companion object {
        @JvmField
        val MARK_LOCATIONS: Array<Location?> = arrayOf<Location?>(Location(2657, 3318, 3))
    }
}
