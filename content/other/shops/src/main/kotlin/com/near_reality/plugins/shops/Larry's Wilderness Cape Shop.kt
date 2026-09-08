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
import com.zenyte.game.item.ItemId.TEAM13_CAPE
import com.zenyte.game.item.ItemId.TEAM23_CAPE
import com.zenyte.game.item.ItemId.TEAM33_CAPE
import com.zenyte.game.item.ItemId.TEAM3_CAPE
import com.zenyte.game.item.ItemId.TEAM43_CAPE

class LarrySWildernessCapeShop : ShopScript() {

    init {
        "Larry's Wilderness Cape Shop"(243, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM3_CAPE(100, 30, 50)
            TEAM13_CAPE(100, 30, 50)
            TEAM23_CAPE(100, 30, 50)
            TEAM33_CAPE(100, 30, 50)
            TEAM43_CAPE(100, 30, 50)
        }
    }
}
