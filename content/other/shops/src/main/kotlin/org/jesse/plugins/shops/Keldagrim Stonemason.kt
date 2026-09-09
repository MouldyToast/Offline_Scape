package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class KeldagrimStonemason : ShopScript() {

    init {
        "Keldagrim Stonemason"(195, ShopCurrency.COINS, STOCK_ONLY) {
            LIMESTONE_BRICK(1000, 7, 26)
            MARBLE_BLOCK(20, 108333, 325000)
            GOLD_LEAF_8784(20, 43333, 130000)
            MAGIC_STONE_8788(10, 325000, 975000)
        }
    }
}
