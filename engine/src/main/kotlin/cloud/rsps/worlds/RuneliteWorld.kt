package cloud.rsps.worlds

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class RuneliteWorld(
    val id: UShort,
    val types: List<RuneliteWorldType>,
    val address: String,
    val activity: String,
    val location: Byte,
    val players: UShort,
)
