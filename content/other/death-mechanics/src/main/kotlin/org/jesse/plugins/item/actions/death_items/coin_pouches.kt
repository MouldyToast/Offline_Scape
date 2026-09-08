package org.jesse.plugins.item.actions.death_items

import org.jesse.game.content.skills.thieving.CoinPouch
import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class CoinPouchesItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(CoinPouch.ITEMS.keys)

        death {
            val pouch = CoinPouch.ITEMS[item.id]
            lost { yield(Item(COINS_995, org.jesse.plugins.item.CoinPouch.getCoinAmount(pouch, item.amount))) }
            status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
        }
    }
}
