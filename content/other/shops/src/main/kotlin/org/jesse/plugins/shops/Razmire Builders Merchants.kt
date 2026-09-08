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

class RazmireBuildersMerchants : ShopScript() {

    init {
        "Razmire Builders Merchants"(175, ShopCurrency.COINS, STOCK_ONLY) {
            LIMESTONE(1000, 9, 10)
            LIMESTONE_BRICK(1000, 19, 21)
            TIMBER_BEAM(1000, 0, 1)
            SWAMP_PASTE(1000, 28, 31)
            PLANK(10, 0, 1)
        }
    }
}
