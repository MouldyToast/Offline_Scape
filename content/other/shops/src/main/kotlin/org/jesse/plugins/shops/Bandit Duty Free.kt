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

class BanditDutyFree : ShopScript() {

    init {
        "Bandit Duty Free"(237, ShopCurrency.COINS, CAN_SELL, 0.6) {
            POT(5, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, 84, 126)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            BRONZE_PICKAXE(5, 0, 1)
            BRONZE_AXE(10, 10, 14)
        }
    }
}
