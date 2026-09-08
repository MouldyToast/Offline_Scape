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
import com.zenyte.game.item.ItemId.BAILING_BUCKET
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.POT_OF_FLOUR
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.SHEARS
import com.zenyte.game.item.ItemId.SWAMP_PASTE
import com.zenyte.game.item.ItemId.TINDERBOX

class KhazardGeneralStore : ShopScript() {

    init {
        "Khazard General Store"(87, ShopCurrency.COINS, CAN_SELL) {
            BRONZE_PICKAXE(5, 0, 1)
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 196)
            SHEARS(2, 0, 1)
            BUCKET(2, 0, 2)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            ROPE(30, 7, 25)
            POT_OF_FLOUR(1, 4, 15)
            BAILING_BUCKET(30, 4, 15)
            SWAMP_PASTE(500, 12, 42)
            KNIFE(10, 2, 8)
        }
    }
}
