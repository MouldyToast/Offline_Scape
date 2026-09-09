package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class BettySMagicEmporium : ShopScript() {

    init {
        "Betty's Magic Emporium"(15, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(5000, 2, 4)
            WATER_RUNE(5000, 2, 4)
            AIR_RUNE(5000, 2, 4)
            EARTH_RUNE(5000, 2, 4)
            MIND_RUNE(5000, 1, 3)
            BODY_RUNE(5000, 1, 3)
            CHAOS_RUNE(250, 54, 96)
            DEATH_RUNE(250, 108, 220)
            FIRE_RUNE_PACK(80, 258, 430)
            WATER_RUNE_PACK(80, 258, 430)
            AIR_RUNE_PACK(80, 258, 430)
            EARTH_RUNE_PACK(80, 258, 430)
            MIND_RUNE_PACK(40, 198, 330)
            CHAOS_RUNE_PACK(35, 5970, 9950)
            EYE_OF_NEWT(300, 1, 3)
            EYE_OF_NEWT_PACK(60, 180, 300)
            BLUE_WIZARD_HAT(1, 1, 2)
            WIZARD_HAT(1, 1, 2)
        }
    }
}
