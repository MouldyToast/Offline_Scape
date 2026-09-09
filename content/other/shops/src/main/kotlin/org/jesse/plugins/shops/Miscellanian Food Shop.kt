package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class MiscellanianFoodShop : ShopScript() {

    init {
        "Miscellanian Food Shop"(49, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(5, 6, 12)
            CHEESE(5, 2, 4)
            CABBAGE(5, 0, 1)
            POTATO(5, 0, 1)
            ONION(5, 1, 3)
            POT_OF_FLOUR(5, 5, 10)
            CHOCOLATE_BAR(2, 5, 10)
            BUCKET_OF_MILK(5, 3, 6)
        }
    }
}
