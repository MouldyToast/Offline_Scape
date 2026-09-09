package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class BobSBrilliantAxes : ShopScript() {

    init {
        "Bob's Brilliant Axes"(156, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(5, 0, 1)
            BRONZE_AXE(10, 9, 16)
            IRON_AXE(5, 33, 56)
            STEEL_AXE(3, 112, 200)
            IRON_BATTLEAXE(5, 112, 182)
            STEEL_BATTLEAXE(2, 403, 650)
            MITHRIL_BATTLEAXE(1, 1047, 1690)
        }
    }
}
