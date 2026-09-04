package com.near_reality.plugins.item

import com.zenyte.game.content.grandexchange.ItemSets
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class BarrowsSetsUnpackItemaction : ItemActionScript() {

    init {
        items(TORAGS_ARMOUR_SET, DHAROKS_ARMOUR_SET, KARILS_ARMOUR_SET, AHRIMS_ARMOUR_SET, GUTHANS_ARMOUR_SET, VERACS_ARMOUR_SET)

        "Unpack" {
            ItemSets.unpack(player, this.item.id)
        }
    }
}
