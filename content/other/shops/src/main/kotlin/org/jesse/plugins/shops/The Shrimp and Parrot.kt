package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

class TheShrimpAndParrot : ShopScript() {

    init {
        "The Shrimp and Parrot"(103, ShopCurrency.COINS, STOCK_ONLY) {
            HERRING(5, 11, 19)
            COD(5, 18, 32)
            TUNA(5, 75, 130)
            LOBSTER(3, 112, 195)
            SWORDFISH(2, 150, 260)
            COOKED_KARAMBWAN(3, 187, 325)
        }
    }
}
