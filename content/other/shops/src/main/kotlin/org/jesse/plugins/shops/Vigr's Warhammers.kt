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

class VigrSWarhammers : ShopScript() {

    init {
        "Vigr's Warhammers"(198, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_WARHAMMER(5, 25, 61)
            IRON_WARHAMMER(4, 94, 224)
            STEEL_WARHAMMER(3, 352, 832)
            BLACK_WARHAMMER(3, 539, 1274)
            MITHRIL_WARHAMMER(2, 913, 2158)
            ADAMANT_WARHAMMER(1, 2266, 5356)
        }
    }
}
