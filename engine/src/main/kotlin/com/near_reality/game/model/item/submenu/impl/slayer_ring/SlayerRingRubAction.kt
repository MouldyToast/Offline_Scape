package com.near_reality.game.model.item.submenu.impl.slayer_ring

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.NotificationSettings
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.item.EnchantedGem.ActivateDialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class SlayerRingRubAction(
    private val contactMaster: Int = 0,
    private val partner: Int = 1,
    private val openLog: Int = 2,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        when (selectedItemIndex) {
            contactMaster -> player.dialogueManager.start(ActivateDialogue(player))
            partner -> {
                player.interfaceHandler.sendInterface(InterfacePosition.CENTRAL, 68)
                player.slayer.refreshPartnerInterface()
            }
            openLog -> player.notificationSettings.sendKillLog(NotificationSettings.SLAYER_NPC_NAMES, true)
        }
    }
}