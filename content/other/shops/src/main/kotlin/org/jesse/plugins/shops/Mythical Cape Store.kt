package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class MythicalCapeStore : ShopScript() {

    init {
        "Mythical Cape Store"(29, ShopCurrency.COINS, STOCK_ONLY) {
            MYTHICAL_CAPE_22114(50, 0, 10000)
        }
    }
}
