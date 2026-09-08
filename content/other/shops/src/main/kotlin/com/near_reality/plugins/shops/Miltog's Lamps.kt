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
import com.zenyte.game.item.ItemId.BULLSEYE_LANTERN
import com.zenyte.game.item.ItemId.BULLSEYE_LANTERN_EMPTY
import com.zenyte.game.item.ItemId.EMPTY_OIL_LAMP
import com.zenyte.game.item.ItemId.EMPTY_OIL_LANTERN
import com.zenyte.game.item.ItemId.LIGHT_ORB
import com.zenyte.game.item.ItemId.MINING_HELMET
import com.zenyte.game.item.ItemId.OIL_LAMP
import com.zenyte.game.item.ItemId.OIL_LANTERN
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.item.ItemId.UNLIT_TORCH

class MiltogSLamps : ShopScript() {

    init {
        "Miltog's Lamps"(146, ShopCurrency.COINS, STOCK_ONLY) {
            UNLIT_TORCH(15, -1, 6)
            EMPTY_OIL_LAMP(4, -1, 37)
            EMPTY_OIL_LANTERN(2, 48, 176)
            BULLSEYE_LANTERN_EMPTY(1, -1, 600)
            MINING_HELMET(1, 540, 900)
            TINDERBOX(10, 0, 1)
            LIGHT_ORB(0, -1, 525)
            OIL_LAMP(0, -1, 42)
            OIL_LANTERN(0, -1, 187)
            BULLSEYE_LANTERN(0, -1, 630)
        }
    }
}
