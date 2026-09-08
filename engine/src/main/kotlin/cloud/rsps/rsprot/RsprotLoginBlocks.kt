package cloud.rsps.rsprot

import com.zenyte.CacheManager
import com.zenyte.game.GameConstants
import com.zenyte.game.net.NetworkConstants
import com.zenyte.game.world.World
import com.zenyte.utils.TimeUnit
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import net.rsprot.protocol.loginprot.outgoing.LoginResponse

/**
 * @author Jire
 */
object RsprotLoginBlocks {

    fun LoginBlock<*>.getInitialResponse(
        isReconnect: Boolean
    ): LoginResponse? {
        if (version != GameConstants.REVISION
            || subVersion != GameConstants.CLIENT_VERSION
        ) {
            return LoginResponse.ClientOutOfDate
        }

        if (siteSettings != NetworkConstants.SITE_SETTINGS) {
            return LoginResponse.BadSessionId
        }

        if (!isReconnect) {
            val updateTimer = World.getUpdateTimer()
            if (updateTimer >= 0
                && updateTimer < TimeUnit.MINUTES.toTicks(1)
            ) {
                return LoginResponse.UpdateInProgress
            }

            val worldProfile = GameConstants.WORLD_PROFILE
            if (worldProfile.useWhitelist
                && !worldProfile.whitelistedUsernames.contains(username)
            ) {
                return LoginResponse.ClosedBetaInvitedOnly
            }
        }

        val serverCrc = CacheManager.getCrc()!!
        if (!crc.validate(serverCrc)) {
            return LoginResponse.OutOfDateReload
        }

        return null
    }

}