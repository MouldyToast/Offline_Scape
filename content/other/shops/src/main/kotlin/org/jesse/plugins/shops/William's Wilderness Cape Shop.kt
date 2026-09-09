package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class WilliamSWildernessCapeShop : ShopScript() {

    init {
        "William's Wilderness Cape Shop"(251, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM1_CAPE(100, 30, 50)
            TEAM11_CAPE(100, 30, 50)
            TEAM21_CAPE(100, 30, 50)
            TEAM31_CAPE(100, 30, 50)
            TEAM41_CAPE(100, 30, 50)
        }
    }
}
