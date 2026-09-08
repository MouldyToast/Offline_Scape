package com.near_reality.api.service

import com.zenyte.game.GameConstants
import com.zenyte.logger.NearRealityLogger

abstract class APIService {

    protected val logger = NearRealityLogger.getLogger(this::class.java)

    val enabled: Boolean get() = GameConstants.WORLD_PROFILE.isApiEnabled()
}
