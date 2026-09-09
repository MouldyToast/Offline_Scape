package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class OziachSArmour : ShopScript() {

    init {
        "Oziach's Armour"(153, ShopCurrency.COINS, STOCK_ONLY) {
            RUNE_PLATEBODY(2, 26000, 84500)
            GREEN_DHIDE_BODY(2, 3120, 10140)
            ANTIDRAGON_SHIELD(35, 8, 26)
        }
    }
}
