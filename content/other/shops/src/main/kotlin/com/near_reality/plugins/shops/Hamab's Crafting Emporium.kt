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
import com.zenyte.game.item.ItemId.BRACELET_MOULD
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.MAMULET_MOULD
import com.zenyte.game.item.ItemId.NECKLACE_MOULD
import com.zenyte.game.item.ItemId.NEEDLE
import com.zenyte.game.item.ItemId.RING_MOULD
import com.zenyte.game.item.ItemId.THREAD

class HamabSCraftingEmporium : ShopScript() {

    init {
        "Hamab's Crafting Emporium"(224, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(10, 1, 14)
            RING_MOULD(10, 1, 5)
            NECKLACE_MOULD(10, 1, 5)
            MAMULET_MOULD(10, 3, 10)
            NEEDLE(10, 1, 1)
            THREAD(100, 1, 4)
            BALL_OF_WOOL(100, 1, 5)
            BRACELET_MOULD(10, 1, 5)
        }
    }
}
