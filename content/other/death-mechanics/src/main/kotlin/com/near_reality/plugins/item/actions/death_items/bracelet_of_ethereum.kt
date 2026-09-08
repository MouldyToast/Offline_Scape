package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

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
