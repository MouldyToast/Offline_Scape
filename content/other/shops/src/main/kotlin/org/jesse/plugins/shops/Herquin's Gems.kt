package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class HerquinSGems : ShopScript() {

    init {
        "Herquin's Gems"(13, ShopCurrency.COINS, STOCK_ONLY) {
            UNCUT_SAPPHIRE(1, 17, 25)
            UNCUT_EMERALD(0, 35, 50)
            UNCUT_RUBY(0, 70, 100)
            UNCUT_DIAMOND(0, 140, 200)
            SAPPHIRE(1, 175, 250)
            EMERALD(0, 350, 500)
            RUBY(0, 700, 1000)
            DIAMOND(0, 1400, 2000)
        }
    }
}
