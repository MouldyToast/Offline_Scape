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

class ArdougneSpiceStall : ShopScript() {

    init {
        "Ardougne Spice Stall"(82, ShopCurrency.COINS, STOCK_ONLY) {
            SPICE(1, 138, 230)
            KNIFE(1, -1, 6)
            GARLIC(1000, 1, 3, 1)
        }
    }
}
