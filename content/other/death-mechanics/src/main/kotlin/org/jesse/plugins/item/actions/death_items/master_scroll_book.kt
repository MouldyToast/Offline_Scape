package org.jesse.plugins.item.actions.death_items

import org.jesse.game.content.itemtransportation.masterscrolls.MasterScrollBookInterface
import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
