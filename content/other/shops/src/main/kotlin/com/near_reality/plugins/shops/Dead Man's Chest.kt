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
import com.zenyte.game.item.ItemId.GROG
import com.zenyte.game.item.ItemId.KARAMJAN_RUM

class DeadManSChest : ShopScript() {

    init {
        "Dead Man's Chest"(102, ShopCurrency.COINS, STOCK_ONLY) {
            GROG(10, 1, 3)
            KARAMJAN_RUM(10, 9, 27)
        }
    }
}
