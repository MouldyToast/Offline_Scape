package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ArdougneBakerSStall : ShopScript() {

    init {
        "Ardougne Baker's Stall"(78, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(10, 9, 12)
            CAKE(3, 40, 50)
            CHOCOLATE_SLICE(8, 24, 30)
            CHOCOLATE_BAR(7, 8, 10)
        }
    }
}
