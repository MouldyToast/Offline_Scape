package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ArdougneSilverStall : ShopScript() {

    init {
        "Ardougne Silver Stall"(81, ShopCurrency.COINS, STOCK_ONLY) {
            UNSTRUNG_SYMBOL(2, 66, 200)
            SILVER_ORE(1, 25, 75)
            SILVER_BAR(1, 50, 150)
        }
    }
}
