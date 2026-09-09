package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class KeepaKettilonSStore : ShopScript() {

    init {
        "Keepa Kettilon's Store"(38, ShopCurrency.COINS, STOCK_ONLY) {
            TUNA(20, 70, 110)
            SALMON(20, 35, 55)
            COD(20, 17, 27)
            LOBSTER(10, 105, 165)
            SWORDFISH(0, 140, 220)
            SHARK(0, 210, 330)
        }
    }
}
