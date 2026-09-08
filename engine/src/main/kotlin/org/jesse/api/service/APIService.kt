package org.jesse.api.service

import org.jesse.game.GameConstants
import org.jesse.logger.NearRealityLogger

abstract class APIService {

    protected val logger = NearRealityLogger.getLogger(this::class.java)

    val enabled: Boolean get() = GameConstants.WORLD_PROFILE.isApiEnabled()
}
