package org.jesse.api.facade

import org.jesse.api.model.ApiGameMode
import org.jesse.api.model.Bond
import org.jesse.api.responses.UserClaimBondResponse
import org.jesse.api.responses.UserSubtractCreditsResponse
import org.jesse.api.responses.UserUpdateGameModeResponse

interface UserFacade {

    suspend fun findUserIdByUsername(username: String): Long?

    suspend fun hasTwoFactorAuthEnabled(identityId: Long): Boolean
    suspend fun getTwoFactorAuthSecret(identityId: Long): String

    suspend fun setGameMode(identityId: Long, gameMode: ApiGameMode): UserUpdateGameModeResponse

    suspend fun claimBond(identityId: Long, bond: Bond): UserClaimBondResponse

    suspend fun subtractCredits(identityId: Long, amount: Int): UserSubtractCreditsResponse
}
