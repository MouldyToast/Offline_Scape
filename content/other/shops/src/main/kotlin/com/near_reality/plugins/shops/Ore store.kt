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
import com.zenyte.game.item.ItemId.ADAMANTITE_ORE
import com.zenyte.game.item.ItemId.COAL
import com.zenyte.game.item.ItemId.COPPER_ORE
import com.zenyte.game.item.ItemId.GOLD_ORE
import com.zenyte.game.item.ItemId.IRON_ORE
import com.zenyte.game.item.ItemId.MITHRIL_ORE
import com.zenyte.game.item.ItemId.SILVER_ORE
import com.zenyte.game.item.ItemId.TIN_ORE

class OreStore : ShopScript() {

    init {
        "Ore store"(39, ShopCurrency.COINS, STOCK_ONLY) {
            COPPER_ORE(20, 2, 3)
            TIN_ORE(10, 2, 3)
            IRON_ORE(10, 11, 18)
            SILVER_ORE(5, 52, 82)
            COAL(10, 31, 49)
            GOLD_ORE(5, 105, 165)
            MITHRIL_ORE(0, 113, 178)
            ADAMANTITE_ORE(0, 280, 440)
        }
    }
}
