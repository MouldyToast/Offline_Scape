package cloud.rsps.game.hiscores

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class HiscoreSkillData(
    val name: String,
    val level: Short,
    val xp: Int
)
