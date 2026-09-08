package org.jesse.game.world.entity.player.login

import cloud.rsps.net.haproxy.HAProxy.hostAddress
import cloud.rsps.rsprot.RsprotSession
import cloud.rsps.rsprot.Session
import org.jesse.api.GameDatabase
import org.jesse.api.responses.UserLoginResponse
import org.jesse.api.service.user.UserPlayerHandler
import org.jesse.api.service.user.twoFactorEnabled
import org.jesse.game.world.entity.player.onLogin
import org.jesse.security.MFAManager
import org.jesse.cores.CoresManager
import org.jesse.game.GameConstants
import org.jesse.game.net.NetworkConstants
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.PlayerInformation
import org.jesse.utils.TextUtils
import io.netty.channel.ChannelHandlerContext
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import kotlinx.coroutines.runBlocking
import mgi.utilities.StringFormatUtil
import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.api.channel.inetAddress
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import net.rsprot.protocol.loginprot.incoming.util.OtpAuthenticationType
import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import net.rsprot.protocol.loginprot.outgoing.util.AuthenticatorResponse
import org.slf4j.LoggerFactory
import kotlin.jvm.optionals.getOrNull
import net.rsprot.protocol.api.Session as RsprotApiSession

enum class AuthType(val id: Int) {

    TRUSTED_COMPUTER(0),
    TRUSTED_AUTHENTICATION(1),
    NORMAL(2),
    UNTRUSTED_AUTHENTICATION(3);

    companion object {

        @JvmField
        val values = values()

        private val idToType: Int2ObjectMap<AuthType> = Int2ObjectOpenHashMap(values.size)

        init {
            for (value in values) {
                idToType.put(value.id, value)
            }
        }

        @JvmStatic
        operator fun get(id: Int): AuthType? = idToType.get(id)

    }
}

data class AuthenticatorInfo(
    val type: AuthType,
    val code: Int,
    val identifier: Int
)

data class LoginPacketIn(
    val username: String,
    val password: String,
    val authInfo: AuthenticatorInfo?,
    val crcs: IntArray,
) {

    constructor(block: LoginBlock<*>) : this(
        username = block.username,
        password = when (val auth = block.authentication) {
            is AuthenticationType.PasswordAuthentication -> auth.password.asString()
            is XteaKey -> {
                val existing = World.getPlayer(StringFormatUtil.formatUsername(block.username)).getOrNull()
                existing?.playerInformation?.plainPassword ?: error("No player online to grab credentials from.")
            }

            is AuthenticationType.TokenAuthentication -> error("Authentication block hit.")
            else -> throw IllegalArgumentException("Unsupported authentication type: $auth")
        },

        //(block.authentication as? AuthenticationType.TokenAuthentication).toAuthInfo(),
        authInfo = when (val auth = block.authentication) {
            is AuthenticationType.PasswordAuthentication -> auth.toAuthInfo()
            is XteaKey -> {
                throw IllegalArgumentException("Unsupported authentication type: $auth")
            }

            is AuthenticationType.TokenAuthentication -> auth.toAuthInfo()
            else -> throw IllegalArgumentException("Unsupported authentication type: $auth")
        },
        crcs = block.crc.toIntArray()
    )

    fun getResponse(): LoginResponse? {
        if (World.getPlayers().size >= NetworkConstants.PLAYER_CAP)
            return LoginResponse.ServerFull

        val formattedUsername = TextUtils.formatNameForProtocol(username)
        if (formattedUsername.isEmpty() || formattedUsername.length > 12 || !TextUtils.isValidName(formattedUsername))
            return LoginResponse.InvalidUsernameOrPassword

        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LoginPacketIn) return false

        if (username != other.username) return false
        if (password != other.password) return false
        if (!crcs.contentEquals(other.crcs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + crcs.contentHashCode()
        return result
    }
}

private fun AuthenticationType.PasswordAuthentication?.toAuthInfo(): AuthenticatorInfo? {
    if (this == null) return null

    return when (val otpAuth = this.otpAuthentication) {
        is OtpAuthenticationType.TrustedComputer -> AuthenticatorInfo(
            type = AuthType.TRUSTED_COMPUTER,
            code = 0,
            identifier = otpAuth.identifier
        )

        is OtpAuthenticationType.TrustedAuthenticator -> AuthenticatorInfo(
            type = AuthType.TRUSTED_AUTHENTICATION,
            code = otpAuth.otp,
            identifier = 0
        )

        is OtpAuthenticationType.UntrustedAuthentication -> AuthenticatorInfo(
            type = AuthType.UNTRUSTED_AUTHENTICATION,
            code = otpAuth.otp,
            identifier = 0
        )

        is OtpAuthenticationType.NoMultiFactorAuthentication -> AuthenticatorInfo(
            type = AuthType.NORMAL,
            code = 0,
            identifier = 0
        )
    }
}

private fun AuthenticationType.TokenAuthentication?.toAuthInfo(): AuthenticatorInfo? {
    if (this == null) return null

    return when (val otpAuth = this.otpAuthentication) {
        is OtpAuthenticationType.TrustedComputer -> AuthenticatorInfo(
            type = AuthType.TRUSTED_COMPUTER,
            code = 0,
            identifier = otpAuth.identifier
        )

        is OtpAuthenticationType.TrustedAuthenticator -> AuthenticatorInfo(
            type = AuthType.TRUSTED_AUTHENTICATION,
            code = otpAuth.otp,
            identifier = 0
        )

        is OtpAuthenticationType.UntrustedAuthentication -> AuthenticatorInfo(
            type = AuthType.UNTRUSTED_AUTHENTICATION,
            code = otpAuth.otp,
            identifier = 0
        )

        is OtpAuthenticationType.NoMultiFactorAuthentication -> AuthenticatorInfo(
            type = AuthType.NORMAL,
            code = 0,
            identifier = 0
        )
    }
}

data class DetailedLoginResponse(
    val loginResponse: LoginResponse,
    val authenticatorResponse: AuthenticatorResponse?
)

fun DetailedLoginResponse.isValid() = loginResponse is LoginResponse.InQueue
fun DetailedLoginResponse.hasMFAInfo() = authenticatorResponse != null

fun LoginRequest.promptAuthenticator() : DetailedLoginResponse = DetailedLoginResponse(LoginResponse.Authenticator, null)
fun LoginRequest.invalidCredentials() : DetailedLoginResponse = DetailedLoginResponse(LoginResponse.InvalidUsernameOrPassword, null)
fun LoginRequest.invalidAuthCode() : DetailedLoginResponse = DetailedLoginResponse(LoginResponse.InvalidAuthenticatorCode, null)
fun LoginRequest.genericFailure() : DetailedLoginResponse = DetailedLoginResponse(LoginResponse.InvalidLoginPacket, null)
fun LoginRequest.closedBeta() : DetailedLoginResponse = DetailedLoginResponse(LoginResponse.ClosedBeta, null)
fun LoginRequest.success(trust: Boolean = false, player: Player? = null) : DetailedLoginResponse =
    if(trust && player != null) DetailedLoginResponse(LoginResponse.InQueue, AuthenticatorResponse.AuthenticatorCode(player.authenticator.randomUID))
    else DetailedLoginResponse(LoginResponse.InQueue, AuthenticatorResponse.NoAuthenticator)

/**
 * @author Jire
 */
class LoginRequest(
    private val request: LoginPacketIn,
    private val info: PlayerInformation,
    private val ctx: ChannelHandlerContext
) {

    private fun getLoginResponses(player: Player?): DetailedLoginResponse {
        val response = request.getResponse()
        if (response != null)
            return DetailedLoginResponse(response, null)

        val username = info.username
        if (username.isEmpty()
            || username.first().equals('_', true)
            || username.last().equals('_', true)
            || username.first().equals(' ', true)
            || username.last().equals(' ', true)
            || username.length > 12
            || !TextUtils.isValidName(username)
        )
            return invalidCredentials()

        val world = GameConstants.WORLD_PROFILE

        if (!world.verifyPasswords)
            return success()

        if (player != null) {
            if (!world.isApiEnabled()) {
                if (world.isMainDatabaseEnabled()) {
                    val loginResponse = runBlocking {
                        GameDatabase.validateUserLogin(request, world.isBeta())
                    }
                    return when (loginResponse) {
                        UserLoginResponse.InvalidPassword,
                        UserLoginResponse.UserNotExist -> invalidCredentials()
                        UserLoginResponse.LoginFromRestrictedIP -> DetailedLoginResponse(LoginResponse.Banned, null)
                        UserLoginResponse.MFARequired -> promptAuthenticator()
                        UserLoginResponse.NotAuthorizedForBeta -> closedBeta()

                        is UserLoginResponse.MFAValidationRequired -> {
                            player.authenticator.isEnabled = true
                            val type = AuthType[loginResponse.type]

                            if(type == AuthType.TRUSTED_COMPUTER) {
                                val validationResponse = player.authenticator.validate(loginResponse.authCode)
                                if(validationResponse == null) {
                                    player.dbUsername = loginResponse.user.name
                                    return success()
                                }
                                return DetailedLoginResponse(validationResponse, null)
                            }

                            if(type == AuthType.NORMAL) return genericFailure()

                            if(type == AuthType.TRUSTED_AUTHENTICATION) {
                                if(MFAManager.validate(loginResponse.secret, loginResponse.authCode)) {
                                    player.user = loginResponse.user
                                    player.dbUsername = loginResponse.user.name
                                    player.authenticator.trust()
                                    return success(true, player)
                                }
                                return invalidAuthCode()
                            }

                            if(type == AuthType.UNTRUSTED_AUTHENTICATION) {
                                if(!MFAManager.validate(loginResponse.secret, loginResponse.authCode))
                                    return invalidAuthCode()

                                player.user = loginResponse.user
                                player.dbUsername = loginResponse.user.name
                                return success()
                            }

                            return genericFailure()
                        }

                        is UserLoginResponse.Success -> {
                            player.user = loginResponse.user
                            player.dbUsername = loginResponse.user.name
                            return success()
                        }
                    }
                } else {
                    if (request.password != player.playerInformation.plainPassword) {
                        return invalidCredentials()
                    }
                }
            } else {
                val loginResult = UserPlayerHandler.validateLogin(player, ctx.inetAddress().hostAddress)
                if (loginResult != null) {
                    return DetailedLoginResponse(loginResult, null)
                }

                if (world.verify2FA()) {
                    if (player.twoFactorEnabled) {
                        player.authenticator.isEnabled = true
                        val authInfo = request.authInfo
                        when (val type = authInfo?.type) {
                            AuthType.NORMAL -> return promptAuthenticator()
                            AuthType.TRUSTED_AUTHENTICATION,
                            AuthType.UNTRUSTED_AUTHENTICATION -> {
                                if (authInfo.code > 0) {
                                    if (!UserPlayerHandler.validate2FA(player, authInfo.code))
                                        return invalidAuthCode()
                                    if (type == AuthType.TRUSTED_AUTHENTICATION) {
                                        player.authenticator.trust()
                                        return success(true, player)
                                    }
                                    return success()
                                }
                                return invalidAuthCode()
                            }

                            AuthType.TRUSTED_COMPUTER -> {
                                val result = player.authenticator.validate(authInfo.identifier)
                                if (result != null) return promptAuthenticator()
                                return success()
                            }

                            null -> return promptAuthenticator()
                        }
                    } else
                        player.authenticator.isEnabled = true
                }
            }
        }
        if (World.containsPlayer(username))
            return DetailedLoginResponse(LoginResponse.Duplicate, null)

        return success()
    }

    fun loaded(
        player: Player?,
        timeout: Boolean,
        block: LoginBlock<*>,
        handler: GameLoginResponseHandler<Session>
    ) {
        if (timeout) {
            sendFailureResponse(handler, LoginResponse.IPLimit)
            return
        }
        if (player == null) {
            sendFailureResponse(handler, LoginResponse.LoginServerLoadError)
            return
        }

        val responses = getLoginResponses(player)
        val loginResponse = responses.loginResponse
        val authenticatorResponse = responses.authenticatorResponse

        if (!responses.isValid()) {
            sendFailureResponse(handler, loginResponse)
            return
        }

        val auth = block.authentication
        if (auth is XteaKey) {
            val existing = World.getPlayerByUsername(StringFormatUtil.formatUsername(block.username))
            if (existing == null) {
                handler.writeFailedResponse(LoginResponse.InvalidLoginPacket)
                return
            }

            val index = existing.index
            if (index == -1) {
                handler.writeFailedResponse(LoginResponse.InvalidLoginPacket)
                return
            }

            if (!existing.playerInformation.seed.contentEquals(auth.key)) {
                handler.writeFailedResponse(LoginResponse.InvalidLoginPacket)
                return
            }

            existing.logout(true, "XTEA Auth logout - this is a big ISSUE")
            WorldTasksManager.schedule(5) {
                val time = System.currentTimeMillis()
                CoresManager.getLoginManager().load(
                    time, player.playerInformation
                ) { player: Player ->
                    WorldTasksManager.schedule {
                        postLogin(player, block, authenticatorResponse, handler, index)
                    }
                }
            }
        } else {
            WorldTasksManager.schedule {
                postLogin(player, block, authenticatorResponse, handler, null)
            }
        }
    }

    private fun postLogin(
        player: Player, block: LoginBlock<*>,
        authenticatorInfo: AuthenticatorResponse?,
        handler: GameLoginResponseHandler<Session>,
        oldIndex: Int?
    ) {
        player.createLogger()
        //we aren't using reconnection rn, just turn this shit off
        val reconnection: Boolean = block.authentication is XteaKey
        if(reconnection) {
            handler.writeFailedResponse(LoginResponse.InvalidLoginPacket)
            return
        }

        val existing = World.getPlayerByUsername(StringFormatUtil.formatUsername(block.username))
        if (existing != null) {
            handler.writeFailedResponse(LoginResponse.Duplicate)
            return
        }

        /* We should never get here, the only time a null auth info gets passed is when the initial info is bad */
        if(authenticatorInfo == null) {
            handler.writeFailedResponse(LoginResponse.InvalidLoginPacket)
            return
        }

        val username = StringFormatUtil.formatUsername(block.username)
        // By this point, the other player should've already logged out
        val old = World.getPlayerByUsername(username)
        val saveRequests = CoresManager.getLoginManager()
        if (reconnection && ((old != null && !old.isFinished) || saveRequests.isInSaveQueue(old))) {
            handler.writeFailedResponse(LoginResponse.Duplicate)
            return
        }

        val index = World.registerPlayer(player)

        if (index == -1) {
            handler.writeFailedResponse(LoginResponse.ServerFull)
            return
        }

        player.areaManager.onLogin(player)
        player.loadMapRegions(true)

        val session = writeSuccessfulResponse(
            handler,
            authenticatorInfo,
            block,
            index,
            player
        )

        try {
            player.index = index
            player.playerInformation.ip = session.hostAddress
            player.session = RsprotSession(
                block.seed,
                player,
                session
            )
            player.allocateInfos()
            try {
                player.packetDispatcher.rebuildLogin()

                session.setDisconnectionHook {
                    logger.info(
                        "'" + player.name
                                + "' has disconnected (RSProt) (index: " + player.index + ")."
                    )
                    player.logout(true, "RSProt Disconnect Hook on session called.")
                }

                player.afterLoadMapRegions()
                player.isInitialized = true
                player.onLogin()
            } catch (t: Throwable) {
                logger.error("Error in login - post-allocation initialization", t)
                player.logout(true, "Error in login - post-allocation initialization")
            }
        } catch (t: Throwable) {
            logger.error("Error in login - initial allocation", t)
            player.logout(true, "Error in login - initial allocation")
        }
    }

    private fun writeSuccessfulResponse(
        response: GameLoginResponseHandler<Session>,
        authenticatorInfo: AuthenticatorResponse,
        block: LoginBlock<*>,
        index: Int,
        player: Player
    ): RsprotApiSession<Session> {
//        val reconnection = block.authentication is XteaKey
//        return if (reconnection) {
//            response.writeSuccessfulResponse(LoginResponse.ReconnectOk(info), block)
//        } else {
        return response.writeSuccessfulResponse(
            LoginResponse.Ok(
                authenticatorInfo,
                2,
                true,
                index,
                true,
                0,
                0,
                0,
            ),
            block
        )
        // }
    }

    private fun sendFailureResponse(handler: GameLoginResponseHandler<Session>, response: LoginResponse) =
        handler.writeFailedResponse(response)

    companion object {
        private val logger = LoggerFactory.getLogger(LoginRequest::class.java)
    }
}
