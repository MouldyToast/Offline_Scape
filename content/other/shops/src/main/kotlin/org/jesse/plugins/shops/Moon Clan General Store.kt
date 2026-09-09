package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class MoonClanGeneralStore : ShopScript() {

    init {
        "Moon Clan General Store"(42, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, 0, 1)
            JUG(2, 0, 1)
            SHEARS(2, 0, 1)
            BUCKET(3, 0, 2)
            BOWL(2, 2, 4)
            CAKE_TIN(2, 5, 10)
            TINDERBOX(3, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            SECURITY_BOOK(5, 0, 2)
        }
    }
}
