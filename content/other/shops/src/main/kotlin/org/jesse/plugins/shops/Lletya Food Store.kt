package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class LletyaFoodStore : ShopScript() {

    init {
        "Lletya Food Store"(181, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(10, -1, 15)
            LOBSTER(15, -1, 195)
            JUG_OF_WINE(3, -1, 1)
            CHEESE(10, -1, 5)
            CAKE(5, -1, 65)
        }
    }
}
