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

class NeilSWildernessCapeShop : ShopScript() {

    init {
        "Neil's Wilderness Cape Shop"(246, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM7_CAPE(100, 30, 50)
            TEAM17_CAPE(100, 30, 50)
            TEAM27_CAPE(100, 30, 50)
            TEAM37_CAPE(100, 30, 50)
            TEAM47_CAPE(100, 30, 50)
        }
    }
}
