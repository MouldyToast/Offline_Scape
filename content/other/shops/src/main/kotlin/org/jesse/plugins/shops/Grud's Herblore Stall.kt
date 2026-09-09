package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class GrudSHerbloreStall : ShopScript() {

    init {
        "Grud's Herblore Stall"(27, ShopCurrency.COINS, STOCK_ONLY) {
            VIAL(50, 0, 2)
            EMPTY_VIAL_PACK(10, 80, 260)
            PESTLE_AND_MORTAR(3, 1, 5)
            EYE_OF_NEWT(50, 1, 3)
            EYE_OF_NEWT_PACK(20, 120, 390)
        }
    }
}
