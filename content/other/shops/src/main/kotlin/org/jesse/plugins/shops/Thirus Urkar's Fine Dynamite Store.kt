package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ThirusUrkarSFineDynamiteStore : ShopScript() {

    init {
        "Thirus Urkar's Fine Dynamite Store"(212, ShopCurrency.COINS, STOCK_ONLY) {
            DYNAMITE(1000, 50, 1150)
            TINDERBOX(2, -1, 11)
        }
    }
}
