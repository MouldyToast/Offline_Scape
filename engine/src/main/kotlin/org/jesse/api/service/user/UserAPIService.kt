package org.jesse.api.service.user

import org.jesse.api.APIClient
import org.jesse.api.ApiConstants
import org.jesse.api.model.ApiGameMode
import org.jesse.api.model.Bond
import org.jesse.api.model.Skill
import org.jesse.api.requests.UserLoginRequest
import org.jesse.api.requests.UserUpdateHiScoresRequest
import org.jesse.api.resources.User
import org.jesse.api.responses.*
import org.jesse.api.service.APIService
import org.jesse.api.util.AES
import kotlinx.coroutines.runBlocking

object UserAPIService : APIService() {

    fun login(username: String, password: String, ip: String, uuid: ByteArray, onResponse: (UserLoginResponse?) -> Unit) {
        if (!APIClient.post<UserLoginRequest, UserLoginResponse, User.Login>(
                resource = User.Login(),
                request = UserLoginRequest(username, runBlocking { AES.encrypt(password, ApiConstants.AES_SECRET)!! }, ip, uuid),
                async = false,
                onSuccess = { onResponse(this) },
                onFailed = {
                    logger.error("Failed to login user: $username (api_status_received=$this)", it)
                    onResponse(null)
                }
            )
        ) onResponse(null)
    }

    fun validate2FA(user: org.jesse.api.model.User, code: Int, onResponse: (Boolean) -> Unit) {
        if(!APIClient.get<Boolean, User.Id.Check2FA>(
                resource = User.Id.Check2FA(user.id, code),
                async = false,
                onSuccess = { onResponse(this) },
                onFailed = { onResponse(false) }
            )
        ) onResponse(false)
    }

    fun updateGameMode(user: org.jesse.api.model.User, mode: ApiGameMode, onResponse: (UserUpdateGameModeResponse?) -> Unit) {
        if (!APIClient.post<UserUpdateGameModeResponse, User.Id.GameMode>(
                resource = User.Id.GameMode(user.id, mode),
                onSuccess = { onResponse(this) },
                onFailed = { onResponse(null) }
            )
        ) onResponse(null)
    }

    fun updateHiscores(user: org.jesse.api.model.User, skills: List<Skill>) {
        APIClient.post<UserUpdateHiScoresRequest, UserUpdateHiScoresResponse, User.Id.Hiscores>(
            resource = User.Id.Hiscores(user.id),
            request = UserUpdateHiScoresRequest(skills)
        )
    }

    fun claimBond(user: org.jesse.api.model.User, bond: Bond, onResponse: (UserClaimBondResponse?) -> Unit) {
        if (!APIClient.post<UserClaimBondResponse, User.Id.Bond>(
                resource = User.Id.Bond(user.id, bond),
                onSuccess = { onResponse(this) },
                onFailed = { onResponse(null) }
            )
        ) onResponse(null)
    }

    fun subtractCredits(user: org.jesse.api.model.User, amount: Int, onResponse: (UserSubtractCreditsResponse?) -> Unit) {
        if (!APIClient.post<UserSubtractCreditsResponse, User.Id.Credits>(
                resource = User.Id.Credits(user.id, amount),
                onSuccess = { onResponse(this) },
                onFailed = { onResponse(null) }
            )
        ) onResponse(null)
    }
}
