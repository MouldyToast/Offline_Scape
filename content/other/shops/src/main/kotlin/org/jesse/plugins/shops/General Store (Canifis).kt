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

class GeneralStoreCanifis : ShopScript() {

    init {
        "General Store (Canifis)"(170, ShopCurrency.COINS, CAN_SELL) {
            NEEDLE(10, 0, 2)
            THREAD(50, 0, 2)
            POT(5, 0, 2)
            BUCKET(3, 0, 4)
            JUG(2, 0, 2)
            EMPTY_JUG_PACK(6, -1, 280)
            TINDERBOX(3, 0, 2)
            CHISEL(2, 0, 2)
            HAMMER(5, 0, 2)
            SAMPLE_BOTTLE(10, 0, 10)
            KNIFE(2, 0, 12)
        }
    }
}
