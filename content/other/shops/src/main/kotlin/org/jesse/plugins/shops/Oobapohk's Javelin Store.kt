package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class OobapohkSJavelinStore : ShopScript() {

    init {
        "Oobapohk's Javelin Store"(225, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_JAVELIN(500, 2, 4)
            IRON_JAVELIN(500, 3, 6)
            STEEL_JAVELIN(500, 14, 24)
            MITHRIL_JAVELIN(500, 38, 64)
            ADAMANT_JAVELIN(500, 96, 160)
            RUNE_JAVELIN(500, 240, 400)
        }
    }
}
