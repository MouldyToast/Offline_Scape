package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class MiltogSLamps : ShopScript() {

    init {
        "Miltog's Lamps"(146, ShopCurrency.COINS, STOCK_ONLY) {
            UNLIT_TORCH(15, -1, 6)
            EMPTY_OIL_LAMP(4, -1, 37)
            EMPTY_OIL_LANTERN(2, 48, 176)
            BULLSEYE_LANTERN_EMPTY(1, -1, 600)
            MINING_HELMET(1, 540, 900)
            TINDERBOX(10, 0, 1)
            LIGHT_ORB(0, -1, 525)
            OIL_LAMP(0, -1, 42)
            OIL_LANTERN(0, -1, 187)
            BULLSEYE_LANTERN(0, -1, 630)
        }
    }
}
