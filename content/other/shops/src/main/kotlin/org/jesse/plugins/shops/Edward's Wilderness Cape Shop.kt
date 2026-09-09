package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class EdwardSWildernessCapeShop : ShopScript() {

    init {
        "Edward's Wilderness Cape Shop"(241, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM5_CAPE(100, 30, 50)
            TEAM15_CAPE(100, 30, 50)
            TEAM25_CAPE(100, 30, 50)
            TEAM35_CAPE(100, 30, 50)
            TEAM45_CAPE(100, 30, 50)
        }
    }
}
