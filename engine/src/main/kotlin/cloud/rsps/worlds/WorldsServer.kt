package cloud.rsps.worlds

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
object WorldsServer {

    @JvmStatic
    fun create(config: WorldsServerConfig): EmbeddedServer<*, *> = embeddedServer(
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

            routing {
                get("/worlds.ws") {
                    val jagexWorldsFile = WorldsManager.jagexWorldsFile
                    if (jagexWorldsFile == null) {
                        call.respond(
                            status = HttpStatusCode.NotFound,
                            message = "Worlds file not loaded yet."
                        )
                        return@get
                    }
                    call.respond(jagexWorldsFile)
                }
                get("/worlds.js") {
                    val runeliteWorlds = WorldsManager.runeliteWorlds
                    if (runeliteWorlds == null) {
                        call.respond(
                            status = HttpStatusCode.NotFound,
                            message = "RuneLite worlds not loaded yet."
                        )
                        return@get
                    }
                    call.respond(runeliteWorlds)
                }
            }
        }
    )

}
