package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ZekeSSuperiorScimitars : ShopScript() {

    init {
        "Zeke's Superior Scimitars"(128, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SCIMITAR(5, 17, 32)
            IRON_SCIMITAR(3, 61, 112)
            STEEL_SCIMITAR(2, 220, 400)
            MITHRIL_SCIMITAR(1, 572, 1040)
        }
    }
}
