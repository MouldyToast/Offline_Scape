package cloud.rsps.game.hiscores

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class HiscoreResponse(
    val skills: List<HiscoreSkill>,
    val activities: List<HiscoreActivity>
)
