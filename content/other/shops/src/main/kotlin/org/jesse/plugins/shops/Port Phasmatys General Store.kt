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

class PortPhasmatysGeneralStore : ShopScript() {

    init {
        "Port Phasmatys General Store"(176, ShopCurrency.COINS, CAN_SELL) {
            POT(100, 1, 1)
            BUCKET(100, 1, 2)
            SHEARS(10, 1, 1)
            JUG(10, 1, 1)
            EMPTY_JUG_PACK(5, 56, 182)
            TINDERBOX(10, 1, 1)
            CHISEL(10, 1, 14)
            HAMMER(10, 1, 13)
        }
    }
}
