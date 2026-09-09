package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class GraceSGracefulClothing : ShopScript() {

    init {
        "Grace's Graceful Clothing"(2, ShopCurrency.MARK_OF_GRACE, STOCK_ONLY) {
            GRACEFUL_HOOD(100, 11, 14)
            GRACEFUL_CAPE(100, 12, 16)
            GRACEFUL_TOP(100, 17, 22)
            GRACEFUL_LEGS(100, 19, 24)
            GRACEFUL_GLOVES(100, 9, 12)
            GRACEFUL_BOOTS(100, 12, 16)
            AMYLASE_PACK(1000, 1, 4)
        }
    }
}
