package org.jesse.game.model.item.submenu.impl.slayer_ring

import org.jesse.game.model.item.submenu.ISubMenuAction
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.world.entity.player.NotificationSettings
import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.item.EnchantedGem.ActivateDialogue

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