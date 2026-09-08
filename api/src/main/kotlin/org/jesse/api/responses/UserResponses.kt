package org.jesse.api.responses

import org.jesse.api.model.User
import kotlinx.serialization.Serializable

@Serializable
sealed class UserLoginResponse {

    @Serializable
    data class Success(val user: User) : UserLoginResponse()

    /* This is only used by the Game Server during direct DB connections */
    @Serializable
    data class MFAValidationRequired(val user: User, val type: Int, val secret: String, val authCode: Int) : UserLoginResponse()

    @Serializable
    data object UserNotExist : UserLoginResponse()

    @Serializable
    data object InvalidPassword : UserLoginResponse()

    @Serializable
    data object LoginFromRestrictedIP : UserLoginResponse()

    @Serializable
    data object NotAuthorizedForBeta : UserLoginResponse()

    @Serializable
    data object MFARequired : UserLoginResponse()
}

@Serializable
sealed class UserUpdateGameModeResponse {

    @Serializable
    data class Success(val user: User) : UserUpdateGameModeResponse()

    @Serializable
    data object UserNotExist : UserUpdateGameModeResponse()
}

@Serializable
sealed class UserUpdateHiScoresResponse {

    @Serializable
    data class Success(val user: User) : UserUpdateHiScoresResponse()

    @Serializable
    data object UserNotExist : UserUpdateHiScoresResponse()
}

@Serializable
sealed class UserClaimBondResponse {

    @Serializable
    data class Success(val user: User) : UserClaimBondResponse()

    @Serializable
    data object UserNotExist : UserClaimBondResponse()
}

@Serializable
sealed class UserSubtractCreditsResponse {

    @Serializable
    data class Success(val user: User) : UserSubtractCreditsResponse()

    @Serializable
    data object UserNotExist : UserSubtractCreditsResponse()

    @Serializable
    data class InsufficientFunds(val difference: Int) : UserSubtractCreditsResponse()
}
