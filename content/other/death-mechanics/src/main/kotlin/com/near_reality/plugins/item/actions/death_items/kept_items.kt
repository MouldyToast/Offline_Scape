package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.OLD_SCHOOL_BOND_UNTRADEABLE
import com.zenyte.game.item.ItemId.SALVE_AMULET
import com.zenyte.game.item.ItemId.SALVE_AMULETEI
import com.zenyte.game.item.ItemId.SALVE_AMULETEI_25278
import com.zenyte.game.item.ItemId.SALVE_AMULETEI_26782
import com.zenyte.game.item.ItemId.SALVE_AMULETI
import com.zenyte.game.item.ItemId.SALVE_AMULETI_25250
import com.zenyte.game.item.ItemId.SALVE_AMULETI_26763
import com.zenyte.game.item.ItemId.SALVE_AMULET_E
import com.zenyte.game.item.ItemId._50_DONATOR_SCROLL

class KeptItemsItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(
            OLD_SCHOOL_BOND_UNTRADEABLE,
            _50_DONATOR_SCROLL,
            32149, 32150, 32151, 32152, 32153, 32154, 32155, 32156,
            SALVE_AMULET, SALVE_AMULET_E, SALVE_AMULETI, SALVE_AMULETEI,
            SALVE_AMULETI_25250, SALVE_AMULETEI_25278, SALVE_AMULETI_26763, SALVE_AMULETEI_26782
        )

        death {
            setAlwaysKeptOnDeath()
            kept { yield(item) }
            status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
