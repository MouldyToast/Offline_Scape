package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class SamSWildernessCapeShop : ShopScript() {

    init {
        "Sam's Wilderness Cape Shop"(248, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM10_CAPE(100, 30, 50)
            TEAM20_CAPE(100, 30, 50)
            TEAM30_CAPE(100, 30, 50)
            TEAM40_CAPE(100, 30, 50)
            TEAM50_CAPE(100, 30, 50)
        }
    }
}
