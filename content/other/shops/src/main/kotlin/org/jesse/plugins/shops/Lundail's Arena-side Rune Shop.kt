package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class LundailSArenaSideRuneShop : ShopScript() {

    init {
        "Lundail's Arena-side Rune Shop"(244, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(100_000, 2, 4)
            WATER_RUNE(100_000, 2, 4)
            AIR_RUNE(100_000, 2, 4)
            EARTH_RUNE(100_000, 2, 4)
            MIND_RUNE(100_000, 1, 3)
            BODY_RUNE(100_000, 1, 3)
            NATURE_RUNE(100_000, 108, 180)
            CHAOS_RUNE(100_000, 54, 90)
            LAW_RUNE(100_000, 144, 240)
            COSMIC_RUNE(100_000, 31, 50)
            DEATH_RUNE(100_000, 108, 180)
        }
    }
}
