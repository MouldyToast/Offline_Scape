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

class EdmondSWildernessCapeShop : ShopScript() {

    init {
        "Edmond's Wilderness Cape Shop"(240, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM8_CAPE(100, 30, 50)
            TEAM18_CAPE(100, 30, 50)
            TEAM28_CAPE(100, 30, 50)
            TEAM38_CAPE(100, 30, 50)
            TEAM48_CAPE(100, 30, 50)
        }
    }
}
