package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class DommikSCraftingStore : ShopScript() {

    init {
        "Dommik's Crafting Store"(123, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(2, 1, 1)
            RING_MOULD(10, 1, 5)
            NECKLACE_MOULD(2, 1, 5)
            AMULET_MOULD(10, 1, 5)
            NEEDLE(3, 1, 1)
            THREAD(100, 1, 1)
            HOLY_MOULD(3, 1, 5)
            SICKLE_MOULD(10, 3, 10)
            TIARA_MOULD(10, 33, 100)
            BOLT_MOULD(10, 8, 25)
            BRACELET_MOULD(5, 1, 5)
        }
    }
}
