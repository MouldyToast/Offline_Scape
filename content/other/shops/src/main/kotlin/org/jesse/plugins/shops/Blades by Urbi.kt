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

class BladesByUrbi : ShopScript() {

    init {
        "Blades by Urbi"(139, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_DAGGER(1, 2, 15)
            IRON_DAGGER(1, 8, 52)
            STEEL_DAGGER(1, 31, 187)
            BLACK_DAGGER(0, 60, 360)
            MITHRIL_DAGGER(1, 81, 487)
            ADAMANT_DAGGER(1, 200, 1200)
            RUNE_DAGGER(1, 2000, 12000)
            DRAGON_DAGGER(0, 7500, 45000)
            BRONZE_SCIMITAR(1, 8, 48)
            IRON_SCIMITAR(1, 28, 168)
            STEEL_SCIMITAR(1, 100, 600)
        }
    }
}
