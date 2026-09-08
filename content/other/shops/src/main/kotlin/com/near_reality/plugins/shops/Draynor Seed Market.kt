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

class DraynorSeedMarket : ShopScript() {

    init {
        "Draynor Seed Market"(150, ShopCurrency.COINS, STOCK_ONLY) {
            POTATO_SEED(100, 0, 2, 10)
            ONION_SEED(60, 1, 3, 10)
            CABBAGE_SEED(30, 1, 3, 10)
            TOMATO_SEED(0, 0, 4)
            SWEETCORN_SEED(0, 0, 9)
            STRAWBERRY_SEED(0, 0, 21)
            WATERMELON_SEED(0, 0, 67)
            BARLEY_SEED(100, 1, 2, 10)
            JUTE_SEED(30, 3, 6, 10)
            ROSEMARY_SEED(100, 2, 4, 10)
            MARIGOLD_SEED(100, 1, 2, 10)
            HAMMERSTONE_SEED(100, 1, 2, 10)
            ASGARNIAN_SEED(60, 1, 3, 10)
            YANILLIAN_SEED(30, 3, 7, 10)
            KRANDORIAN_SEED(10, 0, 9, 10)
            WILDBLOOD_SEED(10, 0, 16, 10)
        }
    }
}
