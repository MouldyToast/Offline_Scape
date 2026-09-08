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

class WarrensGeneralStore : ShopScript() {

    init {
        "Warrens General Store"(218, ShopCurrency.COINS, STOCK_ONLY) {
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, 0, 1)
            BUCKET(2, 0, 2)
            BOWL(2, 1, 5)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            SPADE(5, 1, 3)
            KNIFE(4, 2, 7)
        }
    }
}
