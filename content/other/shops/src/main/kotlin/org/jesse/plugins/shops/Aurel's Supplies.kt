package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class AurelSSupplies : ShopScript() {

    init {
        "Aurel's Supplies"(169, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_AXE(10, 6, 20)
            TINDERBOX(10, 0, 1)
            THIN_SNAIL(10, 2, 6)
            RAW_MACKEREL(10, 6, 22)
        }
    }
}
