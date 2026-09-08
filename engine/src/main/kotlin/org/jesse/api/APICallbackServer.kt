package org.jesse.api

import org.jesse.api.service.sanction.SanctionAPIService
import org.jesse.api.service.store.StoreAPIService
import org.jesse.api.service.vote.VoteAPIService
import org.jesse.game.world.World
import org.jesse.logger.NearRealityLogger
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

object APICallbackServer {

    private val logger = NearRealityLogger.getLogger(this::class.java)

    fun start(callbackServerPort: Int = 6969): Boolean {
        try {
            embeddedServer(Netty, port = callbackServerPort) {
                install(ContentNegotiation) {
                    json()
                }
                install(CallLogging) {
                    level = Level.TRACE
                }
                install(Resources)
                routing {
                    with(StoreAPIService) {
                        orderCallback()
                    }
                    with(VoteAPIService) {
                        voteCallback()
                    }
                    with(SanctionAPIService) {
                        sanctionCallback()
                    }
                    get("/shutmedownplease") {
                        World.shutdown()
                    }
                }
            }.start(wait = false)
            logger.info("Store API callback server started on port {}", callbackServerPort)
            return true
        } catch (e: Exception) {
            logger.error("Failed to start store API callback server", e)
            return false
        }
    }
}
