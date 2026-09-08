package cloud.rsps.worlds

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class WorldsServerConfig(
    val port: Int = DEFAULT_PORT,
    val host: String = DEFAULT_HOST
) {

    companion object {
        const val DEFAULT_PORT = 9292
        const val DEFAULT_HOST = "0.0.0.0"
    }

}
