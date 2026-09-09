package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ContrabandYakProduce : ShopScript() {

    init {
        "Contraband yak produce"(36, ShopCurrency.COINS, STOCK_ONLY) {
            YAKHIDE(25, 35, 55)
            RAW_YAK_MEAT(50, 1, 2)
            HAIR(50, 1, 2)
            CURED_YAKHIDE(10, 70, 110)
        }
    }
}
