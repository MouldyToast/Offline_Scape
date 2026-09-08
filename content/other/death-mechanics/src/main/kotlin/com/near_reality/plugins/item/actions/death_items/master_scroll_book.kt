package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.content.itemtransportation.masterscrolls.MasterScrollBookInterface
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.*

class MasterScrollBookItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(MASTER_SCROLL_BOOK, MASTER_SCROLL_BOOK_EMPTY)

        death {
            lost {
                yield(Item(MASTER_SCROLL_BOOK_EMPTY))
                yieldAll(MasterScrollBookInterface.toItemList(item))
            }
            status {
                if (pvp) {
                    ItemDeathStatus.DROP_ON_DEATH
                } else {
                    ItemDeathStatus.GO_TO_GRAVESTONE
                }
            }
        }
    }
}
