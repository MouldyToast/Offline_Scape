package cloud.rsps.game.hiscores

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class HiscoreActivity(
    val name: String,
    val rank: Long,
    val score: Long
)
