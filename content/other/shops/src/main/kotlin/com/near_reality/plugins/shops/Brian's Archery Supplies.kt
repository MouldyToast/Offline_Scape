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

class BrianSArcherySupplies : ShopScript() {

    init {
        "Brian's Archery Supplies"(21, ShopCurrency.COINS, STOCK_ONLY) {
            STEEL_ARROW(1500, 7, 12)
            MITHRIL_ARROW(1000, 20, 32)
            ADAMANT_ARROW(800, 52, 80)
            OAK_SHORTBOW(4, 65, 100)
            OAK_LONGBOW(4, 104, 160)
            WILLOW_SHORTBOW(3, 130, 200)
            WILLOW_LONGBOW(3, 208, 320)
            MAPLE_SHORTBOW(2, 260, 400)
            MAPLE_LONGBOW(2, 416, 640)
        }
    }
}
