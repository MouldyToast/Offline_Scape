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

class JatixSHerbloreShop : ShopScript() {

    init {
        "Jatix's Herblore Shop"(23, ShopCurrency.COINS, STOCK_ONLY) {
            VIAL(800, 1, 2)
            EMPTY_VIAL_PACK(800, 140, 200)
            PESTLE_AND_MORTAR(3, 2, 4)
            EYE_OF_NEWT(800, 2, 3)
            EYE_OF_NEWT_PACK(100, 210, 300)
        }
    }
}
