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

class GreengrocerOfMiscellania : ShopScript() {

    init {
        "Greengrocer of Miscellania"(46, ShopCurrency.COINS, STOCK_ONLY) {
            CABBAGE(10, 0, 1)
            POTATO(10, 0, 1)
            ONION(10, 2, 3)
            TOMATO(10, 3, 4)
            GARLIC(2, 2, 3)
        }
    }
}
