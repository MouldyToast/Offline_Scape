package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class LegendsGuildGeneralStore : ShopScript() {

    init {
        "Legends Guild General Store"(60, ShopCurrency.COINS, STOCK_ONLY) {
            SWORDFISH(20, -1, 310)
            APPLE_PIE(5, -1, 46)
            ATTACK_POTION3(3, -1, 18)
            STEEL_ARROW(500, -1, 18)
        }
    }
}
