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
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.TINDERBOX

class LittleMuntySLittleShop : ShopScript() {

    init {
        "Little Munty's Little Shop"(210, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(20, 0, 1)
            POT(50000, 0, 1)
            JUG(5, 0, 1)
            EMPTY_JUG_PACK(4, 56, 154)
            BUCKET(5, 0, 1)
            BOWL(5, 1, 4)
            TINDERBOX(2, 0, 1)
            BALL_OF_WOOL(30, 0, 2)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
        }
    }
}
