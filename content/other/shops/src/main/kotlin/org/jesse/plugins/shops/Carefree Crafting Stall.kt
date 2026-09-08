package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

class CarefreeCraftingStall : ShopScript() {

    init {
        "Carefree Crafting Stall"(185, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(2, 0, 1)
            RING_MOULD(4, 4, 7)
            NECKLACE_MOULD(2, 4, 7)
            NEEDLE(3, 0, 1)
            THREAD(100, 0, 1)
            BALL_OF_WOOL(100, 1, 3)
        }
    }
}
