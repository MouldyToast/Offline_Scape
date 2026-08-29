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

class AlryTheAnglerSAnglingAccessories : ShopScript() {

    init {
        "Alry The Angler's Angling Accessories"(293, ShopCurrency.MOLCH_PEARL, STOCK_ONLY) {
            PEARL_FISHING_ROD(1000, 0, 100)
            PEARL_FLY_FISHING_ROD(1000, 0, 120)
            PEARL_BARBARIAN_ROD(1000, 0, 150)
            FISH_SACK(1000, 0, 1000)
            ANGLER_HAT(1000, 0, 100)
            ANGLER_TOP(1000, 0, 100)
            ANGLER_WADERS(1000, 0, 100)
            ANGLER_BOOTS(1000, 0, 100)
        }
    }
}
