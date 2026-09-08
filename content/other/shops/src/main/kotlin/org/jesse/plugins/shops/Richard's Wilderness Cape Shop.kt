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

class RichardSWildernessCapeShop : ShopScript() {

    init {
        "Richard's Wilderness Cape Shop"(247, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM6_CAPE(100, 30, 50)
            TEAM16_CAPE(100, 30, 50)
            TEAM26_CAPE(100, 30, 50)
            TEAM36_CAPE(100, 30, 50)
            TEAM46_CAPE(100, 30, 50)
        }
    }
}
