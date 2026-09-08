package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

class VoidKnightArcheryStore : ShopScript() {

    init {
        "Void Knight Archery Store"(235, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_JAVELIN(10, 2, 4)
            IRON_JAVELIN(10, 3, 6)
            STEEL_JAVELIN(10, 14, 24)
            MITHRIL_JAVELIN(5, 38, 64)
            ADAMANT_JAVELIN(5, 96, 160)
            RUNE_JAVELIN(5, 240, 400)
            BRONZE_ARROWTIPS(10, 0, 1)
            IRON_ARROWTIPS(10, 1, 2)
            STEEL_ARROWTIPS(10, 3, 6)
            MITHRIL_ARROWTIPS(5, 9, 16)
            ADAMANT_ARROWTIPS(5, 24, 40)
            RUNE_ARROWTIPS(5, 120, 200)
        }
    }
}
