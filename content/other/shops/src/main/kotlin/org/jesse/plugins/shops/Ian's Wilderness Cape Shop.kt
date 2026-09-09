package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class IanSWildernessCapeShop : ShopScript() {

    init {
        "Ian's Wilderness Cape Shop"(242, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM2_CAPE(100, 30, 50)
            TEAM12_CAPE(100, 30, 50)
            TEAM22_CAPE(100, 30, 50)
            TEAM32_CAPE(100, 30, 50)
            TEAM42_CAPE(100, 30, 50)
        }
    }
}
