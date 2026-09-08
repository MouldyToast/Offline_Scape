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

class LittleMuntySLittleShop : ShopScript() {

    init {
        "Little Munty's Little Shop"(210, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(20, 0, 1)
            POT(50000, 0, 1)
            JUG(5, 0, 1)
            EMPTY_JUG_PACK(4, 56, 154)
            BUCKET(5, 0, 1)
            BOWL(5, 1, 4)
            TINDERBOX(2, 0, 1)
            BALL_OF_WOOL(30, 0, 2)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
        }
    }
}
