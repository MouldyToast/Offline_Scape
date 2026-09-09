package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class MagicGuildStoreMysticRobes : ShopScript() {

    init {
        "Magic Guild Store (Mystic Robes)"(70, ShopCurrency.COINS, STOCK_ONLY) {
            MYSTIC_HAT(100, 3600, 9000)
            MYSTIC_ROBE_TOP(100, 28800, 72000)
            MYSTIC_ROBE_BOTTOM(100, 19200, 48000)
            MYSTIC_GLOVES(100, 2400, 6000)
            MYSTIC_BOOTS(100, 2400, 6000)
        }
    }
}
