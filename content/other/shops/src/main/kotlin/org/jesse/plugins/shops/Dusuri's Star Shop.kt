package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class DusuriSStarShop : ShopScript() {

    init {
        "Dusuri's Star Store"(1005, ShopCurrency.STARDUST, STOCK_ONLY) {
            CELESTIAL_RING_UNCHARGED(50, 1600, 2000)
            STAR_FRAGMENT(50, 2400, 3000)
            BAG_FULL_OF_GEMS(100, 240, 300)
            SOFT_CLAY_PACK(1000, 120, 150)
            452(5000, 0, 50) // noted runite ore
            450(5000, 0, 35) // noted adamantite ore
        }
    }
}
