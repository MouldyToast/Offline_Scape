package org.jesse.plugins.item

import org.jesse.game.content.grandexchange.ItemSets
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class BarrowsSetsUnpackItemaction : ItemActionScript() {

    init {
        items(TORAGS_ARMOUR_SET, DHAROKS_ARMOUR_SET, KARILS_ARMOUR_SET, AHRIMS_ARMOUR_SET, GUTHANS_ARMOUR_SET, VERACS_ARMOUR_SET)

        "Unpack" {
            ItemSets.unpack(player, this.item.id)
        }
    }
}
