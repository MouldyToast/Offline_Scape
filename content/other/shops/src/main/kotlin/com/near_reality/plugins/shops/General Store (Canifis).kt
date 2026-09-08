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
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.NEEDLE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.SAMPLE_BOTTLE
import com.zenyte.game.item.ItemId.THREAD
import com.zenyte.game.item.ItemId.TINDERBOX

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
