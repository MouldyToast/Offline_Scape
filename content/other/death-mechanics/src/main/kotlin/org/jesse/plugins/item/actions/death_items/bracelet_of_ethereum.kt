package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class BraceletOfEthereumItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(BRACELET_OF_ETHEREUM, BRACELET_OF_ETHEREUM_UNCHARGED)

        death {
            setAlwaysLostOnDeath()
            lost {
                yield(Item(BRACELET_OF_ETHEREUM_UNCHARGED))
                if (item.charges > 0) {
                    yield(Item(REVENANT_ETHER, item.charges))
                }
            }
            status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
        }
    }
}
