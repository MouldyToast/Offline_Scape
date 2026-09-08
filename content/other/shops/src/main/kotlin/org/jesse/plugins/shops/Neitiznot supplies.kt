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

class NeitiznotSupplies : ShopScript() {

    init {
        "Neitiznot supplies"(50, ShopCurrency.COINS, STOCK_ONLY) {
            KNIFE(10, 1, 6)
            HAMMER(10, 0, 1)
            THREAD(10, 0, 1)
            NEEDLE(10, 0, 1)
            BRONZE_AXE(10, 4, 16)
            BALL_OF_WOOL(100, 0, 2)
        }
    }
}
