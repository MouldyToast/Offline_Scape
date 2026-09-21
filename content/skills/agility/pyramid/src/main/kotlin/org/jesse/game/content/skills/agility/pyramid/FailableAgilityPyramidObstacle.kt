package org.jesse.game.content.skills.agility.pyramid

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.Failable
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.`object`.WorldObject
import kotlin.math.max

abstract class FailableAgilityPyramidObstacle(index: Int) : AgilityCourseObstacle(AgilityPyramid::class.java, index),
    Failable {
    open fun permanentSuccessLevel(): Int {
        return 70
    }

    override fun successful(player: Player, `object`: WorldObject): Boolean {
        val level = player.getSkills().getLevel(SkillConstants.AGILITY)
        val baseRequirement = 30
        val baseChance = 75 //Base chance % to not fail minimum level.
        val neverFailLevel = permanentSuccessLevel()
        if (level > neverFailLevel) return true
        val adjustmentPercentage = 100 - baseChance
        val successPerLevel = adjustmentPercentage.toFloat() / (neverFailLevel.toFloat() - baseRequirement)
        val successChance = baseChance + max(0, (level - baseRequirement)) * successPerLevel
        return Utils.random(100) < successChance
    }
}
