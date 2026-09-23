package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ScavvoSRuneStore : ShopScript() {

    init {
        "Scavvo's Rune Store"(55, ShopCurrency.COINS, STOCK_ONLY) {
            RUNE_PLATESKIRT(1, 38400, 64000)
            RUNE_PLATELEGS(1, 38400, 64000)
            RUNE_MACE(1, 8640, 14400)
            RUNE_CHAINBODY(1, 30000, 50000)
            RUNE_LONGSWORD(1, 19200, 32000)
            RUNE_SWORD(1, 12480, 20800)
            GREEN_DHIDE_CHAPS(1, 2340, 3900)
            GREEN_DHIDE_VAMBRACES(1, 1500, 2500)
            COIF(2, 120, 200)
        }
    }
}
