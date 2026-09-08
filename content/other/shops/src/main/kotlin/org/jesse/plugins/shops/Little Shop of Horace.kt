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

class LittleShopOfHorace : ShopScript() {

    init {
        "Little Shop of Horace"(205, ShopCurrency.COINS, CAN_SELL) {
            VIAL_OF_WATER(60, 0, 2)
            WATERFILLED_VIAL_PACK(30, 196, 251)
            POT(30, 0, 1)
            JUG(5, 0, 1)
            EMPTY_JUG_PACK(6, -1, 175)
            BUCKET(5, 0, 2)
            BOWL(5, 0, 5)
            MACHETE(1, 28, 50)
            SHEARS(2, 0, 1)
            ROPE(30, 12, 22)
            TINDERBOX(2, 1, 1)
            SPADE(5, 0, 3)
            BALL_OF_WOOL(30, 1, 2)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            NEEDLE(2, 0, 1)
            THREAD(40, 0, 1)
        }
    }
}
