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
import com.zenyte.game.item.ItemId.BALL_OF_WOOL
import com.zenyte.game.item.ItemId.BOWL
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.MACHETE
import com.zenyte.game.item.ItemId.NEEDLE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.SHEARS
import com.zenyte.game.item.ItemId.SPADE
import com.zenyte.game.item.ItemId.THREAD
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.item.ItemId.VIAL_OF_WATER
import com.zenyte.game.item.ItemId.WATERFILLED_VIAL_PACK

class LittleShopOfHorace : ShopScript() {

    init {
        "Little Shop of Horace"(205, ShopCurrency.COINS, CAN_SELL) {
            VIAL_OF_WATER(60, 0, 2)
            WATERFILLED_VIAL_PACK(30, 196, 251)
            POT(30, 0, 1)
            JUG(5, 0, 1)
            EMPTY_JUG_PACK(6, -1, 175)
            BUCKET(5, 0, 2)
            BOWL(5, 0, 5)
            MACHETE(1, 28, 50)
            SHEARS(2, 0, 1)
            ROPE(30, 12, 22)
            TINDERBOX(2, 1, 1)
            SPADE(5, 0, 3)
            BALL_OF_WOOL(30, 1, 2)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            NEEDLE(2, 0, 1)
            THREAD(40, 0, 1)
        }
    }
}
