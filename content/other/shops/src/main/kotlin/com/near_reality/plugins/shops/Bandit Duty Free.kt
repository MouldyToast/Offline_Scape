package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.zenyte.game.item.ItemId.BRONZE_AXE
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.TINDERBOX

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
