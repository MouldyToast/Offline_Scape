package org.jesse.game.content.boss.nex.item.equip

import org.jesse.scripts.item.equip.EquipHandlerResponse
import org.jesse.scripts.item.equip.ItemEquipScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class TorvaItemequip : ItemEquipScript() {

    init {
        items(TORVA_FULLHELM_DAMAGED, TORVA_PLATEBODY_DAMAGED, TORVA_PLATELEGS_DAMAGED)

        intercept {
            val type = when(item.id) {
                TORVA_FULLHELM_DAMAGED -> "helm"
                TORVA_PLATEBODY_DAMAGED -> "platebody"
                TORVA_PLATELEGS_DAMAGED -> "platelegs"
                else -> error("Unknown torva type $item")
            }
            player.sendMessage("This $type is currently too malformed to equip. It needs repairing somehow.")
            EquipHandlerResponse.Negate
        }
    }
}
