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

class IfabaSGeneralStore : ShopScript() {

    init {
        "Ifaba's General Store"(222, ShopCurrency.COINS, CAN_SELL) {
            POT(3, 1, 1)
            JUG(2, 1, 1)
            EMPTY_JUG_PACK(8, -1, 140)
            ROPE(8, 5, 18)
            BUCKET(2, 1, 2)
            TINDERBOX(2, 1, 1)
            HAMMER(5, 1, 13)
        }
    }
}
