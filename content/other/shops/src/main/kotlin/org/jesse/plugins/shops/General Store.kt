package org.jesse.plugins.shops

import org.jesse.ContentConstants
import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class GeneralStore : ShopScript() {

    init {
        "${ContentConstants.SERVER_NAME} General Store"(152, ShopCurrency.COINS, CAN_SELL) {
            POT(5, 0, 1)
            JUG(2, 0, 1)
            SHEARS(2, 0, 1)
            BUCKET(3, 0, 2)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 4, 13)
            TINDERBOX(3, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            NEWCOMER_MAP(5, 0, 1)
            SECURITY_BOOK(5, 0, 2)
        }
    }
}
