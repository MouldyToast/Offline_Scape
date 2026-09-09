package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class HendorSAwesomeOres : ShopScript() {

    init {
        "Hendor's Awesome Ores"(63, ShopCurrency.COINS, STOCK_ONLY) {
            COPPER_ORE(0, 2, 3)
            TIN_ORE(0, 2, 3)
            IRON_ORE(0, 11, 17)
            MITHRIL_ORE(0, 113, 162)
            ADAMANTITE_ORE(0, 280, 400)
            RUNITE_ORE(0, 2240, 3200)
            COAL(0, 31, 45)
        }
    }
}
