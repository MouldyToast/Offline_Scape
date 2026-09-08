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

class NathifaSBakeStall : ShopScript() {

    init {
        "Nathifa's Bake Stall"(141, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(10, 6, 12)
            CAKE(3, 27, 50)
            CHOCOLATE_SLICE(8, 16, 30)
            WATERSKIN4(50, 16, 30)
        }
    }
}
