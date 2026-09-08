package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ids.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class DarrenSWildernessCapeShop : ShopScript() {

    init {
        "Darren's Wilderness Cape Shop"(239, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM4_CAPE(100, 30, 50)
            TEAM14_CAPE(100, 30, 50)
            TEAM24_CAPE(100, 30, 50)
            TEAM34_CAPE(100, 30, 50)
            TEAM44_CAPE(100, 30, 50)
        }
    }
}
