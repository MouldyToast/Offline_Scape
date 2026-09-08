package cloud.rsps.worlds

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class RuneliteWorlds(
    val worlds: List<RuneliteWorld>,
)
