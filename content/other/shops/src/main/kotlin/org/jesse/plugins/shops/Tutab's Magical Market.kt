package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class TutabSMagicalMarket : ShopScript() {

    init {
        "Tutab's Magical Market"(227, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(1000, 4, 17)
            WATER_RUNE(1000, 4, 17)
            AIR_RUNE(1000, 4, 17)
            EARTH_RUNE(1000, 4, 17)
            LAW_RUNE(250, 126, 240)
            EYE_OF_GNOME(10, 1, 3)
            MONKEY_DENTURES(10, 3, 10)
            MONKEY_TALISMAN(10, 333, 1000)
        }
    }
}
