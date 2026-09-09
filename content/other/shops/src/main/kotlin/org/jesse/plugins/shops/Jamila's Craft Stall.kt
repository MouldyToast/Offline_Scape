package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class JamilaSCraftStall : ShopScript() {

    init {
        "Jamila's Craft Stall"(140, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(2, 0, 1)
            RING_MOULD(4, 1, 7)
            NECKLACE_MOULD(2, 1, 7)
            AMULET_MOULD(2, 1, 7)
            NEEDLE(3, 0, 1)
            THREAD(100, 0, 1)
            HOLY_MOULD(3, 1, 7)
            SICKLE_MOULD(6, 2, 15)
            TIARA_MOULD(10, 25, 150)
            BRACELET_MOULD(5, 1, 7)
        }
    }
}
