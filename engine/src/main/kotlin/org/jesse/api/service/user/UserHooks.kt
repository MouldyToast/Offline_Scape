package org.jesse.api.service.user

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.events.LoginEvent
import org.jesse.plugins.events.LogoutEvent

/**
 * Represents the hooks for user related events.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object UserHooks {

    /**
     * Registers a login event hook that will update the notice board for the player.
     */
    @Subscribe
    @JvmStatic
    fun onLogin(loginEvent: LoginEvent) {
        fun Player.updateNoticeBoard() {
            updateDonationInfoOnPlayerDetailsTab()
            updateVoteStatisticOnPlayerDetailsTab()
        }
        loginEvent.player.updateNoticeBoard()
    }

    /**
     * Registers a logout event hook that will update the hiscores for the player.
     */
    @Subscribe
    @JvmStatic
    fun onLogout(logoutEvent: LogoutEvent) {
        UserPlayerHandler.updateHiscores(logoutEvent.player)
    }
}
