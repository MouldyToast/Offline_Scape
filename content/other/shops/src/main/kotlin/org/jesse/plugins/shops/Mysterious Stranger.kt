package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class MysteriousStranger : ShopScript() {

    init {
        "Mysterious Stranger"(555, ShopCurrency.COINS, STOCK_ONLY) {
            VERZIKS_CRYSTAL_SHARD(9500, 10000, 75000)
        }
    }
}
