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

class Armoury : ShopScript() {

    init {
        "Armoury"(85, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_ARROW(200, 0, 1)
            BRONZE_BOLTS(200, 0, 1)
            SHORTBOW(4, 25, 75)
            LONGBOW(2, 40, 120)
            CROSSBOW(2, 35, 105)
            BRONZE_ARROWTIPS(800, 0, 1)
            IRON_ARROWTIPS(800, 1, 3)
            STEEL_ARROWTIPS(800, 3, 9)
            MITHRIL_ARROWTIPS(800, 8, 24)
            IRON_AXE(5, 28, 84)
            STEEL_AXE(3, 100, 300)
            IRON_BATTLEAXE(5, 91, 273)
            STEEL_BATTLEAXE(2, 325, 975)
            MITHRIL_BATTLEAXE(1, 845, 2535)
            BRONZE_2H_SWORD(4, 40, 120)
            IRON_2H_SWORD(3, 140, 420)
            STEEL_2H_SWORD(2, 500, 1500)
            BLACK_2H_SWORD(1, 960, 2880)
            MITHRIL_2H_SWORD(1, 1300, 3900)
            ADAMANT_2H_SWORD(1, 3200, 9600)
        }
    }
}
