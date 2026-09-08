package com.near_reality.game.content.araxxor.items

import com.near_reality.game.util.Ticker
import com.zenyte.game.item.ids.*
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-02
 */
class AraneaBoots(
    val player: Player
) {

    private val specialAttackTimer: Ticker = Ticker(8)

    fun processBootTick() {
        if (!specialAttackTimer.finished)
            specialAttackTimer.tick()
    }

    fun isPlayerWearingBoots(): Boolean = player.equipment.containsItem(ARANEA_BOOTS)

    fun isPlayerWebImmune(): Boolean {
        if (isPlayerWearingBoots()) {
            if (specialAttackTimer.finished) {
                player.sendMessage("Your Aranea boots let you avoid the spider-based attack.")
                specialAttackTimer.reset()
                return true
            }
        }
        return false
    }

}