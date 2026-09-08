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

class VoidKnightGeneralStore : ShopScript() {

    init {
        "Void Knight General Store"(234, ShopCurrency.COINS, CAN_SELL) {
            POT(2, 0, 1)
            JUG(2, 0, 11)
            EMPTY_JUG_PACK(5, 56, 182)
            SHEARS(2, 0, 1)
            BUCKET(3, 0, 2)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 4, 13)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            BRONZE_AXE(10, 6, 20)
            FIELD_RATION(25, 120, 390)
        }
    }
}
