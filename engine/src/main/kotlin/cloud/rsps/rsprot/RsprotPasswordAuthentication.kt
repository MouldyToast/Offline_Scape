package cloud.rsps.rsprot

import org.jesse.game.world.World
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType.PasswordAuthentication
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock

/**
 * @author Jire
 */
object RsprotPasswordAuthentication {

    fun handle(
        responseHandler: GameLoginResponseHandler<Session>,
        block: LoginBlock<AuthenticationType>,
        auth: PasswordAuthentication
    ) {
        //auth.password.clear() // clear out of memory instantly, since it won't be used.

        World.addLoginRequest(block, responseHandler)
    }

}