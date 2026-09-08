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
import com.zenyte.game.item.ItemId.CHRONICLE
import com.zenyte.game.item.ItemId.TELEPORT_CARD

class DiangoSToyStore : ShopScript() {

    init {
        "Diango's Toy Store"(149, ShopCurrency.COINS, STOCK_ONLY) {
            CHRONICLE(1, 0, 300)
            TELEPORT_CARD(100, 50, 150)
        }
    }
}
