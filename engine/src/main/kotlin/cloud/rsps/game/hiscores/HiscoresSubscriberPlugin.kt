package cloud.rsps.game.hiscores

import com.google.common.eventbus.Subscribe
import org.jesse.game.GameConstants
import org.jesse.plugins.events.LogoutEvent

/**
 * @author Jire
 */
object HiscoresSubscriberPlugin {

    @Subscribe
    @JvmStatic
    fun onLogout(event: LogoutEvent) {
        val player = event.player ?: return
        if (!GameConstants.WORLD_PROFILE.isHiscoreDatabaseEnabled()) {
            return
        }
        player.gameMode.hiscoreMode.manager.updatePlayer(player)
    }

}
