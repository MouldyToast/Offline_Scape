package cloud.rsps.game.hiscores

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class HiscoreSkill(
    val name: String,
    val rank: Long,
    val level: Short,
    val xp: Int
)
