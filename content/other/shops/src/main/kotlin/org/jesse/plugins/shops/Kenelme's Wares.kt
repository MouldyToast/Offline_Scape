package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class KenelmeSWares : ShopScript() {

    init {
        "Kenelme's Wares"(214, ShopCurrency.COINS, STOCK_ONLY) {
            POT_OF_FLOUR(3, 6, 10)
            RAW_BEEF(1, 0, 1)
            CABBAGE(3, 0, 1)
            BANANA(3, 1, 2)
            REDBERRIES(1, 1, 3)
            BREAD(0, 8, 12)
            CHOCOLATE_BAR(1, 6, 10)
            CHEESE(3, 2, 4)
            TOMATO(3, 2, 4)
            POTATO(1, 0, 1)
        }
    }
}
