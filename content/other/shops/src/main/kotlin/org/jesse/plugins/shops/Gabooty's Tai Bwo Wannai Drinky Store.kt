package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class GabootySTaiBwoWannaiDrinkyStore : ShopScript() {

    init {
        "Gabooty's Tai Bwo Wannai Drinky Store"(110, ShopCurrency.COINS, STOCK_ONLY) {
            FRUIT_BLAST(0, 10, 30)
            DRUNK_DRAGON(0, 10, 30)
            PINEAPPLE_PUNCH(0, 10, 30)
            WIZARD_BLIZZARD(0, 10, 30)
            BLURBERRY_SPECIAL(0, 10, 30)
            CHOC_SATURDAY(0, 10, 30)
        }
    }
}
