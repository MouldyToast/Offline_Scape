package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ConstructionSupplies : ShopScript() {

    init {
        "Construction supplies"(161, ShopCurrency.COINS, STOCK_ONLY) {
            SAW(1000, 3, 13)
            BOLT_OF_CLOTH(1000, 195, 650)
            BRONZE_NAILS(95, 1, 2)
            IRON_NAILS(95, 2, 5)
            STEEL_NAILS(95, 1, 3)
        }
    }
}
