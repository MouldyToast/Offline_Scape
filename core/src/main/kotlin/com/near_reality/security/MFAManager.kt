package com.near_reality.security

import com.zenyte.game.GameConstants
import com.warrenstrange.googleauth.GoogleAuthenticator


object MFAManager {
    val ENABLED = GameConstants.WORLD_PROFILE.verify2FA()

    private val googleAuthenticator = GoogleAuthenticator()

    fun validate(secret: String, code: Int) = googleAuthenticator.authorize(secret, code)
}