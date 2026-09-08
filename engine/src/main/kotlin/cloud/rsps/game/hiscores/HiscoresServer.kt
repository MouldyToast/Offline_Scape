package cloud.rsps.game.hiscores

import cloud.rsps.util.Base37
import com.github.michaelbull.logging.InlineLogger
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author Jire
 */
object HiscoresServer {

    private val logger = InlineLogger()

    @JvmStatic
    fun create(config: HiscoresServerConfig): EmbeddedServer<*, *> = embeddedServer(
        Netty,
        environment = applicationEnvironment {},
        configure = {
            connector {
                this.port = config.port
                this.host = config.host
            }
        },
        module = {
            install(DefaultHeaders)

            install(ContentNegotiation) {
                json()
                jackson()
            }

            configureRouting()
        }
    )

    private fun Application.configureRouting() = routing {
        get("/") {
            val modeParam = call.queryParameters["mode"]
            if (modeParam.isNullOrBlank()) {
                call.respondText("Mode is required", status = HttpStatusCode.BadRequest)
                return@get
            }

            val mode = HiscoreMode.fromParamName(modeParam)
            if (mode == null) {
                call.respondText("Invalid mode: $modeParam", status = HttpStatusCode.BadRequest)
                return@get
            }

            val username = call.queryParameters["player"]
            if (username.isNullOrBlank()) {
                call.respondText("Player name is required", status = HttpStatusCode.BadRequest)
                return@get
            }

            val playerId = Base37.encode(username)

            logger.debug { "Received hiscores request for mode: $mode, playerId: $playerId" }

            val response = mode.manager.loadResponse(playerId)
            if (response.skills.isEmpty() || response.activities.isEmpty()) {
                call.respondText("No hiscores data found for player", status = HttpStatusCode.NotFound)
                return@get
            }

            call.respond(response)
        }
    }

}
