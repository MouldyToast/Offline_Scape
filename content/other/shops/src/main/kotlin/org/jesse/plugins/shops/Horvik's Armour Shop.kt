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

class HorvikSArmourShop : ShopScript() {

    init {
        "Horvik's Armour Shop"(163, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_CHAINBODY(5, 36, 60)
            IRON_CHAINBODY(3, 126, 210)
            STEEL_CHAINBODY(3, 450, 750)
            MITHRIL_CHAINBODY(1, 1170, 1950)
            BRONZE_PLATEBODY(3, 96, 160)
            IRON_PLATEBODY(1, 336, 560)
            STEEL_PLATEBODY(1, 1200, 2000)
            BLACK_PLATEBODY(1, 2304, 3840)
            MITHRIL_PLATEBODY(1, 3120, 5200)
            IRON_PLATELEGS(1, 168, 280)
            STUDDED_BODY(1, 510, 850)
            STUDDED_CHAPS(1, 450, 750)
        }
    }
}
