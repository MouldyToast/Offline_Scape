package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class BurthorpeSupplies : ShopScript() {

    init {
        "Burthorpe Supplies"(ShopCurrency.COINS, CAN_SELL) {
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(3, 56, 182)
            SHEARS(2, 0, 1)
            BUCKET(2, 0, 2)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            KNIFE(2, 2, 7)
            EMPTY_CUP(10, 0, 2)
        }
    }
}
