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

class DavonSAmuletStore : ShopScript() {

    init {
        "Davon's Amulet Store"(101, ShopCurrency.COINS, STOCK_ONLY, Shop.DEFAULT_SELL_MULTIPLIER, ShopDiscount.KARAMJA_DIARY) {
            HOLY_SYMBOL(0, 225, 360)
            AMULET_OF_MAGIC(0, 675, 1080)
            AMULET_OF_DEFENCE(0, 956, 1530)
            AMULET_OF_STRENGTH(0, 1215, 2430)
            AMULET_OF_POWER(0, 2643, 4230)
        }
    }
}
