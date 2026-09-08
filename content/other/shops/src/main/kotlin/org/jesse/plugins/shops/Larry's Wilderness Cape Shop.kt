package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

class LarrySWildernessCapeShop : ShopScript() {

    init {
        "Larry's Wilderness Cape Shop"(243, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM3_CAPE(100, 30, 50)
            TEAM13_CAPE(100, 30, 50)
            TEAM23_CAPE(100, 30, 50)
            TEAM33_CAPE(100, 30, 50)
            TEAM43_CAPE(100, 30, 50)
        }
    }
}
