package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class HamabSCraftingEmporium : ShopScript() {

    init {
        "Hamab's Crafting Emporium"(224, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(10, 1, 14)
            RING_MOULD(10, 1, 5)
            NECKLACE_MOULD(10, 1, 5)
            MAMULET_MOULD(10, 3, 10)
            NEEDLE(10, 1, 1)
            THREAD(100, 1, 4)
            BALL_OF_WOOL(100, 1, 5)
            BRACELET_MOULD(10, 1, 5)
        }
    }
}
