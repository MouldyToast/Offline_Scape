package cloud.rsps.rsprot

import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.api.GameConnectionHandler
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import net.rsprot.protocol.loginprot.outgoing.LoginResponse

/**
 * @author Jire
 */
object RsprotGameConnectionHandler : GameConnectionHandler<Session> {

    override fun onLogin(
        responseHandler: GameLoginResponseHandler<Session>,
        block: LoginBlock<AuthenticationType>
    ) {
        val initialResponse = RsprotLoginBlocks.run {
            block.getInitialResponse(false)
        }
        if (initialResponse != null) {
            responseHandler.writeFailedResponse(initialResponse)
            return
        }

        when (val auth = block.authentication) {
            is AuthenticationType.PasswordAuthentication ->
                RsprotPasswordAuthentication.handle(responseHandler, block, auth)

            else ->
                responseHandler.writeFailedResponse(LoginResponse.InvalidLoginPacket)
        }
    }

    override fun onReconnect(
        responseHandler: GameLoginResponseHandler<Session>,
        block: LoginBlock<XteaKey>
    ) {
        val initialResponse = RsprotLoginBlocks.run {
            block.getInitialResponse(true)
        }
        if (initialResponse != null) {
            responseHandler.writeFailedResponse(initialResponse)
            return
        }

        RsprotReconnect.handle(responseHandler, block)
    }

}
