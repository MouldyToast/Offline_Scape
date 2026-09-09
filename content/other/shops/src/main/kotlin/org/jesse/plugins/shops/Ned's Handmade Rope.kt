package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class NedSHandmadeRope : ShopScript() {

    init {
        "Ned's Handmade Rope"(ShopCurrency.COINS, STOCK_ONLY) {
            ROPE(30, 3, 16)
        }
    }
}
