package com.near_reality.api.service.user

import com.near_reality.api.APIClient
import com.near_reality.api.model.Bond
import com.near_reality.api.responses.UserClaimBondResponse
import com.near_reality.api.responses.UserLoginResponse
import com.near_reality.api.responses.UserSubtractCreditsResponse
import com.near_reality.api.service.sanction.isBanned
import com.near_reality.api.service.sanction.isHardwareBlocked
import com.near_reality.api.util.completeWithTimeout
import com.near_reality.game.world.entity.player.flaggedAsBot
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.logger.NearRealityLogger
import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import java.util.concurrent.TimeoutException
import kotlin.time.Duration.Companion.seconds

/**
 * Handles player related user actions.
 */
object UserPlayerHandler {

    private val logger = NearRealityLogger.getLogger(UserPlayerHandler::class.java)
    private val useAPI = false

    /**
     * Validates the login credentials of the [player] and returns a [ClientResponse] based on the result.
     *
     * This method is blocking and should NOT be called from the game thread.
     */
    fun validateLogin(player: Player, ip: String): LoginResponse? {
        val (username, password) = player.playerInformation.let { it.displayname to it.plainPassword }
        val uuid = player.playerInformation.uuid
        try {
            return completeWithTimeout<LoginResponse>(10.seconds) {
                UserAPIService.login(username, password, ip, uuid) { response ->
                    logger.info("Received response from API for $username -> {}", response)
                    val clientResponse = when (response) {
                        null -> LoginResponse.LoginServerOffline
                        UserLoginResponse.InvalidPassword,
                        UserLoginResponse.UserNotExist
                        -> LoginResponse.InvalidUsernameOrPassword

                        is UserLoginResponse.Success -> {
                            val user = response.user
                            player.dbUsername = user.dbName
                            if (user.isBanned) {
                                if (player.flaggedAsBot) // To confuse the botters :D
                                    setOf(
                                        LoginResponse.TooManyAttempts,
                                        LoginResponse.Timeout
                                    ).random()
                                else
                                    LoginResponse.Banned
                            }
                            else if (user.isHardwareBlocked) {
                                setOf(
                                    LoginResponse.LoginServerOffline,
                                    LoginResponse.TooManyAttempts,
                                    LoginResponse.Timeout
                                ).random()
                            }
                            else {
                                player.user = user
                                player.dbUsername = user.dbName
                                null
                            }
                        }

                        UserLoginResponse.NotAuthorizedForBeta -> LoginResponse.NoBetaAccess
                        UserLoginResponse.LoginFromRestrictedIP ->  LoginResponse.Banned
                        /* These routes are never returned by the API, only the direct connection to the database */
                        UserLoginResponse.MFARequired -> LoginResponse.ServiceUnavailable
                        is UserLoginResponse.MFAValidationRequired -> LoginResponse.ServiceUnavailable
                    }
                    complete(clientResponse)
                }
            }
        } catch (e: TimeoutException) {
            logger.warn("API response timed out for login request of $username")
            return LoginResponse.Timeout
        } catch (e: Exception) {
            logger.error("Failed to validate login for $username", e)
            return LoginResponse.LoginServerOffline
        }
    }

    /**
     * Validates the two-factor authentication code of the [player] and returns a [Boolean] based on the result.
     */
    fun validate2FA(player: Player, code: Int): Boolean {
        val user = player.user?:return false
        return completeWithTimeout<Boolean>(10.seconds) {
            UserAPIService.validate2FA(user, code, ::complete)
        } ?:false
    }

    /**
     * Updates the game mode of the [player] and calls the [onResult] callback with the result.
     */
    fun updateGameMode(player: Player, mode: GameMode, onResult: (Boolean) -> Unit = {}) {
        if (!APIClient.enabled) {
            player.sendDeveloperMessage("API is disabled on this world, this will only impact this world's character.")
            player.gameMode = mode
            onResult(true)
            return
        }
        val user = player.user
        if (user == null) {
            onResult(false)
            return
        }
        player.lock()

        UserAPIService.updateGameMode(user, mode.toApi()) { response ->
            WorldTasksManager.schedule(1) {
                player.unlock()
                if (response != null) {
                    player.gameMode = mode
                    onResult(true)
                } else {
                    player.dialogue { plain("Could not update game mode at this point, please try again later.") }
                    onResult(false)
                }
            }
        }
    }

    /**
     * Updates the hiscores of the [player].
     */
    internal fun updateHiscores(player: Player) {
        if (player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR))
            return
        if (player.skills.totalLevel < 50)
            return
        val user = player.user ?: return
//        val skills = (0 until SkillConstants.COUNT).map { skillId ->
//            Skill(skillId, player.skills.getExperience(skillId))
//        }
//        UserService.updateHiscores(user, skills)
    }

    /**
     * Claims a bond for the [player] through the [UserAPIService].
     */
    fun claimBond(player: Player, bond: Bond, name: String) {
        if (!APIClient.enabled) {
            player.dialogue { plain("You cannot claim donator pins on this world.") }
            return
        }
        val user = player.user ?: return
        val donatorPin = Item(bond.id, 1)
        if (player.inventory.deleteItem(donatorPin).result == RequestResult.SUCCESS) {
            player.lock()
            player.dialogue { loading("Claiming $name...") }
            UserAPIService.claimBond(user, bond) {
                WorldTasksManager.schedule {
                    player.lock(1)

                    if (it is UserClaimBondResponse.Success) {
                        player.user = it.user
                        player.dialogue {
                            item(
                                Item(ItemId.COINS_6964),
                                "${Colour.DARK_BLUE.wrap("$${bond.amount}")} has been added to your total spent amount, " +
                                        "making for a total of ${Colour.DARK_BLUE.wrap("$${player.storeTotalSpent}")}."
                            )
                            item(
                                Item(ItemId.DRAGON_TOKEN),
                                "${Colour.RS_GREEN.wrap("${bond.credits} store credits")} have been added to your account, " +
                                        "you now have ${Colour.RS_GREEN.wrap("${player.storeCredits} store credits")}."
                            )
                        }
                    } else {
                        player.inventory.addOrDrop(donatorPin)
                        player.dialogue { item(donatorPin, "Failed to claim your $name your item has been returned.") }
                    }
                }
            }
        }
    }

    fun subtractCredits(player: Player, amount: Int, onResponse: (Boolean) -> Unit) {
        if (!APIClient.enabled) {
            player.dialogue { plain("You cannot make credit shop purchases on this world.") }
            return
        }
        val user = player.user ?: return

        player.lock()
        UserAPIService.subtractCredits(user, amount) {
            WorldTasksManager.schedule {
                player.unlock()
                if (it is UserSubtractCreditsResponse.Success) {
                    player.user = it.user
                    onResponse(true)
                } else {
                    onResponse(false)
                }
            }
        }
    }
}
