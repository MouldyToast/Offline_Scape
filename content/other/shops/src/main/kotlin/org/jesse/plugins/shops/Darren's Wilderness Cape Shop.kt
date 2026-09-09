package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class DarrenSWildernessCapeShop : ShopScript() {

    init {
        "Darren's Wilderness Cape Shop"(239, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM4_CAPE(100, 30, 50)
            TEAM14_CAPE(100, 30, 50)
            TEAM24_CAPE(100, 30, 50)
            TEAM34_CAPE(100, 30, 50)
            TEAM44_CAPE(100, 30, 50)
        }
    }
}
