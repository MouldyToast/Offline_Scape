package org.jesse.api.model

import org.jesse.api.util.toExperience
import org.jesse.api.util.toLevel
import kotlinx.serialization.Serializable

@Serializable
data class UpdateHiscores(
    val username: String,
    val skills: List<Skill>,
)

@Serializable
data class SkillExperience(val skill: Skill, val experience: Int) {
    val level get() = experience.toDouble().toLevel()
}

@Serializable
enum class Skill(val ord: Int, val defaultLevel: Int = 1)  {
    ATTACK(0),
    DEFENCE(1),
    STRENGTH(2),
    HITPOINTS(3, defaultLevel = 10),
    RANGED(4),
    PRAYER(5),
    MAGIC(6),
    COOKING(7),
    WOODCUTTING(8),
    FLETCHING(9),
    FISHING(10),
    FIREMAKING(11),
    CRAFTING(12),
    SMITHING(13),
    MINING(14),
    HERBLORE(15),
    AGILITY(16),
    THIEVING(17),
    SLAYER(18),
    FARMING(19),
    RUNECRAFTING(20),
    HUNTER(21),
    CONSTRUCTION(22);

    val defaultExperience get() = defaultLevel.toExperience()

    fun isCombat(): Boolean = when (this) {
        ATTACK, DEFENCE, STRENGTH, HITPOINTS, RANGED, PRAYER, MAGIC -> true
        else -> false
    }
    fun formatName(): String = name.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    companion object {
        val defaultTotalLevel by lazy { entries.sumOf { it.defaultLevel } }
        val defaultTotalExperience by lazy { entries.sumOf { it.defaultExperience } }
    }
}
