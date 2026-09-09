package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class KeptItemsItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(
            OLD_SCHOOL_BOND_UNTRADEABLE,
            _50_DONATOR_SCROLL,
            32149, 32151, 32152, 32153, 32154, 32155, 32156,
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
