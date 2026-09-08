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

class DrogoSMiningEmporium : ShopScript() {

    init {
        "Drogo's Mining Emporium"(5, ShopCurrency.COINS, STOCK_ONLY) {
            HAMMER(4, 0, 1)
            BRONZE_PICKAXE(4, 0, 1)
            COPPER_ORE(0, 1, 3)
            TIN_ORE(0, 1, 3)
            IRON_ORE(0, 5, 17)
            COAL(0, 13, 45)
            BRONZE_BAR(0, 2, 8)
            IRON_BAR(0, 8, 28)
            GOLD_BAR(0, 90, 300)
        }
    }
}
