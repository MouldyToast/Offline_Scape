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

class LletyaSeamstress : ShopScript() {

    init {
        "Lletya Seamstress"(182, ShopCurrency.COINS, STOCK_ONLY) {
            THREAD(8, 0, 1)
            NEEDLE(3, 0, 1)
            BALL_OF_WOOL(5, 1, 2)
            RED_DYE(10, 2, 6)
            YELLOW_DYE(10, 2, 6)
            BLUE_DYE(10, 2, 6)
            ORANGE_DYE(10, 2, 6)
            GREEN_DYE(10, 2, 6)
            PURPLE_DYE(10, 2, 6)
        }
    }
}
