package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class TheDeeperLode : ShopScript() {

    init {
        "The Deeper Lode"(209, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(10, -1, 2)
            DWARVEN_STOUT(1, -1, 2)
            DRAGON_BITTER(3, -1, 2)
            KEBAB(2, -1, 4)
            BEER_GLASS(0, -1, 2)
        }
    }
}
