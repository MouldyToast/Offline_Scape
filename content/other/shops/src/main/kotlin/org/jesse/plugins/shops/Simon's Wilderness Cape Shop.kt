package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class SimonSWildernessCapeShop : ShopScript() {

    init {
        "Simon's Wilderness Cape Shop"(249, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM9_CAPE(100, 30, 50)
            TEAM19_CAPE(100, 30, 50)
            TEAM29_CAPE(100, 30, 50)
            TEAM39_CAPE(100, 30, 50)
            TEAM49_CAPE(100, 30, 50)
        }
    }
}
