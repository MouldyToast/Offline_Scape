package org.jesse.api.util

import org.jesse.api.ApiConstants
import org.jesse.api.requests.UserLoginRequest


suspend fun UserLoginRequest.decryptPassword() =
    AES.decrypt(password, ApiConstants.AES_SECRET)
