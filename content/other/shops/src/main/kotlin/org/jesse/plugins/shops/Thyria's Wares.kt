package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ThyriaSWares : ShopScript() {

    init {
        "Thyria's Wares"(202, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(5000, 2, 4)
            WATER_RUNE(5000, 2, 4)
            AIR_RUNE(5000, 2, 4)
            EARTH_RUNE(5000, 2, 4)
            MIND_RUNE(5000, 1, 3)
            BODY_RUNE(5000, 1, 3)
            CHAOS_RUNE(250, -1, 90)
            DEATH_RUNE(150, -1, 267)
            FIRE_RUNE_PACK(80, -1, 430)
            WATER_RUNE_PACK(80, -1, 430)
            AIR_RUNE_PACK(80, -1, 430)
            EARTH_RUNE_PACK(80, -1, 430)
            MIND_RUNE_PACK(40, -1, 330)
            CHAOS_RUNE_PACK(35, -1, 9950)
        }
    }
}
