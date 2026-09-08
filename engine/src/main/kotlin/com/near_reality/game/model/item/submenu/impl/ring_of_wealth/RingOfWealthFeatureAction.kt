package com.near_reality.game.model.item.submenu.impl.ring_of_wealth

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.zenyte.game.world.entity.player.NotificationSettings
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Setting
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.logger.NearRealityLogger

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class RingOfWealthFeatureAction(
    private val bossLog: Int = 0,
    private val coinCollector: Int = 1
): ISubMenuAction {

    private val logger = NearRealityLogger.getLogger(RingOfWealthFeatureAction::class.java)

    override fun onAction(player: Player, selectedItemIndex: Int) {
        when(selectedItemIndex) {
            bossLog -> player.notificationSettings.sendKillLog(NotificationSettings.BOSS_NPC_NAMES, true)
            coinCollector -> player.toggleCoinCollection()
            else -> logger.warn("Unused action index in MaxCapeFeatureAction: $selectedItemIndex")
        }
    }

    private fun Player.toggleCoinCollection() {
        val collectionMode = getBooleanSetting(Setting.ROW_CURRENCY_COLLECTOR)
        settings.setSetting(Setting.ROW_CURRENCY_COLLECTOR, if (collectionMode) 0 else 1)
        dialogue { plain("Currency collector has been turned ${if (!collectionMode) "off." else "on."}") }
    }
}