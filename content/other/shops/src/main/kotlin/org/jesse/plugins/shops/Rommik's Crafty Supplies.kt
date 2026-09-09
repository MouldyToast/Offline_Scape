package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class RommikSCraftySupplies : ShopScript() {

    init {
        "Rommik's Crafty Supplies"(22, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(2, 0, 1)
            RING_MOULD(4, 3, 5)
            NECKLACE_MOULD(2, 3, 5)
            AMULET_MOULD(2, 3, 5)
            NEEDLE(3, 0, 1)
            THREAD(100, 0, 1)
            HOLY_MOULD(3, 3, 5)
            SICKLE_MOULD(6, 6, 10)
            TIARA_MOULD(10, 65, 100)
            BOLT_MOULD(10, 16, 25)
            BRACELET_MOULD(5, 3, 5)
        }
    }
}
