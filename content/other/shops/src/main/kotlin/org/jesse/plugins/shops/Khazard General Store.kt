package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class KhazardGeneralStore : ShopScript() {

    init {
        "Khazard General Store"(87, ShopCurrency.COINS, CAN_SELL) {
            BRONZE_PICKAXE(5, 0, 1)
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 196)
            SHEARS(2, 0, 1)
            BUCKET(2, 0, 2)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            ROPE(30, 7, 25)
            POT_OF_FLOUR(1, 4, 15)
            BAILING_BUCKET(30, 4, 15)
            SWAMP_PASTE(500, 12, 42)
            KNIFE(10, 2, 8)
        }
    }
}
