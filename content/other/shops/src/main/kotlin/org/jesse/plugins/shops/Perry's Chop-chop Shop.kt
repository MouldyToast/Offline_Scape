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

class PerrySChopChopShop : ShopScript() {

    init {
        "Perry's Chop-chop Shop"(207, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_AXE(4, 9, 51)
            IRON_AXE(3, 33, 179)
            STEEL_AXE(2, 120, 640)
            MITHRIL_AXE(1, 312, 1664)
            ADAMANT_AXE(1, 768, 4096)
            RUNE_AXE(1, 7680, 40960)
            TINDERBOX(2, 0, 3)
        }
    }
}
