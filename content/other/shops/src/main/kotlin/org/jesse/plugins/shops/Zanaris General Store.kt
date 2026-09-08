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

class ZanarisGeneralStore : ShopScript() {

    init {
        "Zanaris General Store"(252, ShopCurrency.COINS, CAN_SELL) {
            POT(5, 1, 1)
            JUG(2, 1, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, 1, 1)
            BUCKET(3, 1, 2)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 3, 13)
            TINDERBOX(2, 1, 1)
            CHISEL(2, 1, 1)
            HAMMER(5, 1, 1)
            NEWCOMER_MAP(5, 1, 1)
            SECURITY_BOOK(5, 1, 2)
        }
    }
}
