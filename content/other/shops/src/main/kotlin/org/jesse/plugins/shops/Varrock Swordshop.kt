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

class VarrockSwordshop : ShopScript() {

    init {
        "Varrock Swordshop"(166, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SWORD(5, 15, 26)
            IRON_SWORD(4, 54, 91)
            STEEL_SWORD(4, 195, 325)
            BLACK_SWORD(3, 374, 624)
            MITHRIL_SWORD(3, 507, 845)
            ADAMANT_SWORD(2, 1248, 2080)
            BRONZE_LONGSWORD(4, 24, 40)
            IRON_LONGSWORD(3, 84, 140)
            STEEL_LONGSWORD(3, 300, 500)
            BLACK_LONGSWORD(2, 576, 960)
            MITHRIL_LONGSWORD(2, 780, 1300)
            ADAMANT_LONGSWORD(1, 1920, 3200)
            BRONZE_DAGGER(10, 6, 10)
            IRON_DAGGER(6, 21, 35)
            STEEL_DAGGER(5, 75, 125)
            BLACK_DAGGER(4, 144, 240)
            MITHRIL_DAGGER(3, 195, 325)
            ADAMANT_DAGGER(2, 480, 800)
        }
    }
}
