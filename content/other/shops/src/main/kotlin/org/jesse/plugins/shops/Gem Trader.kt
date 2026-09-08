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

class GemTrader : ShopScript() {

    init {
        "Gem Trader"(124, ShopCurrency.COINS, STOCK_ONLY) {
            UNCUT_SAPPHIRE(1, 8, 25)
            UNCUT_EMERALD(1, 36, 50)
            UNCUT_RUBY(0, 70, 100)
            UNCUT_DIAMOND(0, 140, 200)
            SAPPHIRE(1, 69, 250)
            EMERALD(1, 350, 500)
            RUBY(0, 555, 1000)
            DIAMOND(0, 666, 2000)
        }
    }
}
