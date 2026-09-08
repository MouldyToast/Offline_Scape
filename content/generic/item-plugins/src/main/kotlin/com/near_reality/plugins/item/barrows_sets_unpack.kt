package com.near_reality.plugins.item

import com.zenyte.game.content.grandexchange.ItemSets
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.AHRIMS_ARMOUR_SET
import com.zenyte.game.item.ItemId.DHAROKS_ARMOUR_SET
import com.zenyte.game.item.ItemId.GUTHANS_ARMOUR_SET
import com.zenyte.game.item.ItemId.KARILS_ARMOUR_SET
import com.zenyte.game.item.ItemId.TORAGS_ARMOUR_SET
import com.zenyte.game.item.ItemId.VERACS_ARMOUR_SET

class BarrowsSetsUnpackItemaction : ItemActionScript() {

    init {
        items(TORAGS_ARMOUR_SET, DHAROKS_ARMOUR_SET, KARILS_ARMOUR_SET, AHRIMS_ARMOUR_SET, GUTHANS_ARMOUR_SET, VERACS_ARMOUR_SET)

        "Unpack" {
            ItemSets.unpack(player, this.item.id)
        }
    }
}
