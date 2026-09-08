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
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.SHEARS
import com.zenyte.game.item.ItemId.TINDERBOX

class NardahGeneralStore : ShopScript() {

    init {
        "Nardah General Store"(133, ShopCurrency.COINS, CAN_SELL) {
            POT(5, -1, 1)
            JUG(2, -1, 1)
            EMPTY_JUG_PACK(7, -1, 182)
            SHEARS(2, -1, 1)
            BUCKET(3, -1, 2)
            BOWL(2, -1, 5)
            CAKE_TIN(2, -1, 13)
            TINDERBOX(2, -1, 1)
            CHISEL(2, -1, 1)
            HAMMER(2, -1, 1)
        }
    }
}
