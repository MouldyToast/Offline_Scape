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

class ZeneshaSPlateMailBodyShop : ShopScript() {

    init {
        "Zenesha's Plate Mail Body Shop"(83, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PLATEBODY(3, 53, 160)
            IRON_PLATEBODY(1, 186, 560)
            STEEL_PLATEBODY(1, 666, 2000)
            BLACK_PLATEBODY(1, 1280, 3840)
            MITHRIL_PLATEBODY(1, 1733, 5200)
        }
    }
}
