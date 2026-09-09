package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class FilaminaSWares : ShopScript() {

    init {
        "Filamina's Wares"(200, ShopCurrency.COINS, STOCK_ONLY) {
            STAFF(5, 9, 15)
            MAGIC_STAFF(5, 120, 200)
            STAFF_OF_AIR(2, 900, 1500)
            STAFF_OF_WATER(2, 900, 1500)
            STAFF_OF_EARTH(2, 900, 1500)
            STAFF_OF_FIRE(2, 900, 1500)
        }
    }
}
