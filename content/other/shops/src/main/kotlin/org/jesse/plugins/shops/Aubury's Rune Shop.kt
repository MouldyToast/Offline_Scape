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

class AuburySRuneShop : ShopScript() {

    init {
        "Aubury's Rune Shop"(160, ShopCurrency.COINS, STOCK_ONLY) {
            AIR_RUNE(5000, 2, 4)
            MIND_RUNE(5000, 1, 3)
            FIRE_RUNE(5000, 2, 4)
            WATER_RUNE(5000, 2, 4)
            EARTH_RUNE(5000, 2, 4)
            BODY_RUNE(5000, 1, 3)
            CHAOS_RUNE(250, 90, 90)
            DEATH_RUNE(250, 99, 180)
            FIRE_RUNE_PACK(80, 236, 430)
            WATER_RUNE_PACK(80, 236, 430)
            AIR_RUNE_PACK(80, 236, 430)
            EARTH_RUNE_PACK(80, 236, 430)
            MIND_RUNE_PACK(40, 181, 330)
            CHAOS_RUNE_PACK(35, 5472, 9950)
        }
    }
}
