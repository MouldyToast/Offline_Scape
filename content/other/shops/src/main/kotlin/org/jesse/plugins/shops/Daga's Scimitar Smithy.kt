package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class DagaSScimitarSmithy : ShopScript() {

    init {
        "Daga's Scimitar Smithy"(223, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SCIMITAR(10, 2, 32)
            IRON_SCIMITAR(10, 2, 112)
            STEEL_SCIMITAR(8, 2, 400)
            MITHRIL_SCIMITAR(6, 2, 1040)
            DRAGON_SCIMITAR(4, 1, 100000)
        }
    }
}
