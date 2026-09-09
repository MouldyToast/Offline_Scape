package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class RegathSWares : ShopScript() {

    init {
        "Regath's Wares"(201, ShopCurrency.COINS, STOCK_ONLY) {
            VIAL_OF_WATER(30, -1, 2)
            WATERFILLED_VIAL_PACK(25, -1, 281)
            ROPE(10, -1, 25)
            FIRE_RUNE(500, 2, 5)
            WATER_RUNE(500, 2, 5)
            AIR_RUNE(500, 2, 5)
            EARTH_RUNE(500, 2, 5)
            MIND_RUNE(300, 1, 4)
            BODY_RUNE(300, 1, 4)
            BLOOD_RUNE(50, -1, 560)
            SOUL_RUNE(50, -1, 420)
            FIRE_RUNE_PACK(80, -1, 602)
            WATER_RUNE_PACK(80, -1, 602)
            AIR_RUNE_PACK(80, -1, 602)
            EARTH_RUNE_PACK(80, -1, 602)
            MIND_RUNE_PACK(40, -1, 462)
            BLUE_WIZARD_HAT(1, 1, 2)
            WIZARD_HAT(1, 1, 2)
            EYE_OF_NEWT(300, 1, 4)
            EYE_OF_NEWT_PACK(60, -1, 420)
        }
    }
}
