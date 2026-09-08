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
import com.zenyte.game.item.ItemId.BOWL
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CAKE_TIN
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.FLAMTAER_HAMMER
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.OLIVE_OIL4
import com.zenyte.game.item.ItemId.OLIVE_OIL_PACK
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.POT_OF_FLOUR
import com.zenyte.game.item.ItemId.SHEARS
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.item.ItemId.VIAL_OF_WATER
import com.zenyte.game.item.ItemId.WATERFILLED_VIAL_PACK

class RazmireGeneralStore : ShopScript() {

    init {
        "Razmire General Store"(174, ShopCurrency.COINS, CAN_SELL) {
            POT(5, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, 0, 1)
            BUCKET(3, 0, 2)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 4, 13)
            TINDERBOX(3, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            OLIVE_OIL4(150, 8, 26)
            OLIVE_OIL_PACK(20, 1148, 3471)
            VIAL_OF_WATER(100, 0, 2)
            WATERFILLED_VIAL_PACK(50, 80, 261)
            POT_OF_FLOUR(100, 4, 13)
            FLAMTAER_HAMMER(3, 4333, 13000)
        }
    }
}
