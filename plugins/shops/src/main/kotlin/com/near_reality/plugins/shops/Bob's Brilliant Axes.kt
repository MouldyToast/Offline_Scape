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

class BobSBrilliantAxes : ShopScript() {

    init {
        "Bob's Brilliant Axes"(156, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(5, 0, 1)
            BRONZE_AXE(10, 9, 16)
            IRON_AXE(5, 33, 56)
            STEEL_AXE(3, 112, 200)
            IRON_BATTLEAXE(5, 112, 182)
            STEEL_BATTLEAXE(2, 403, 650)
            MITHRIL_BATTLEAXE(1, 1047, 1690)
        }
    }
}
