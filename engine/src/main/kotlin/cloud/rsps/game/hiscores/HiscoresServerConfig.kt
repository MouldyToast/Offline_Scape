package cloud.rsps.game.hiscores

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class HiscoresServerConfig(
    val port: Int = DEFAULT_PORT,
    val host: String = DEFAULT_HOST
) {

    companion object {
        const val DEFAULT_PORT = 9293
        const val DEFAULT_HOST = "0.0.0.0"
    }

}
