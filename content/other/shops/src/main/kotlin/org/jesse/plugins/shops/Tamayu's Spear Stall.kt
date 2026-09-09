package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class TamayuSSpearStall : ShopScript() {

    init {
        "Tamayu's Spear Stall"(111, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SPEARKP(10, 0, 26)
            IRON_SPEARKP(10, 0, 91)
            STEEL_SPEARKP(5, 0, 325)
            MITHRIL_SPEARKP(2, 0, 845)
            ADAMANT_SPEARKP(0, 0, 2080)
            RUNE_SPEARKP(0, 0, 20800)
            CLEANING_CLOTH(10, 45, 60)
        }
    }
}
