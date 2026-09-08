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

class LeenzSGeneralSupplies : ShopScript() {

    init {
        "Leenz's General Supplies"(215, ShopCurrency.COINS, STOCK_ONLY) {
            POT(50000, 0, 1)
            JUG(5, 0, 1)
            EMPTY_JUG_PACK(6, -1, 154)
            BUCKET(5, 0, 2)
            BOWL(5, 0, 4)
            TINDERBOX(2, 1, 1)
            HAMMER(5, 0, 1)
            BRONZE_NAILS(500, 0, 2)
            IRON_NAILS(500, 1, 4)
        }
    }
}
