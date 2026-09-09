package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ReldakSLeatherArmour : ShopScript() {

    init {
        "Reldak's Leather Armour"(147, ShopCurrency.COINS, STOCK_ONLY) {
            FROGLEATHER_BODY(50, 400, 1000)
            FROGLEATHER_CHAPS(50, 360, 900)
            FROGLEATHER_BOOTS(50, 80, 200)
        }
    }
}
