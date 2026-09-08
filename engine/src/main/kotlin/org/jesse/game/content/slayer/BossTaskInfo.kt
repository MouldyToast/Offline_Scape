package org.jesse.game.content.slayer

import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import java.util.function.Predicate

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 *
 * This is for use in the BossTaskBuilder only.
 */
data class BossTaskInfo(
    override var enumName: String = "",
    var xp: Float = 0f,
    override var predicate: Predicate<Player> = Predicate { true },
    override var monsters: Set<String> = setOf(enumName.lowercase()),
    var isWildernessTask: Boolean = false,
    var assignableByKrystilia: Boolean = false,
    var assignableBySumonaOnly: Boolean = false,
    var validateBlock:     ((String, NPC) -> Boolean)?    = null,
    var experienceBlock:   ((NPC) -> Float)?             = null
) : SlayerTask {
    override fun validate(name: String, npc: NPC): Boolean =
        validateBlock?.invoke(name, npc) ?: name.equals(enumName, ignoreCase = true)

    override fun getExperience(npc: NPC): Float =
        experienceBlock?.invoke(npc) ?: xp

    override val taskId: Int = 98
    override val monsterIds: Set<Int> = emptySet()
    override val slayerRequirement: Int = 0
    override val tip: String = "Not available."
    override var taskName: String = this.enumName
}