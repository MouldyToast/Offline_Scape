package cloud.rsps.game.hiscores

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class HiscoreActivityData(
    val name: String,
    val score: Long
)
