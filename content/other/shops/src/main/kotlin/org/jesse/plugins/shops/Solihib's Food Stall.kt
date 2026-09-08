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

class SolihibSFoodStall : ShopScript() {

    init {
        "Solihib's Food Stall"(226, ShopCurrency.COINS, STOCK_ONLY) {
            MONKEY_NUTS(200, 1, 3)
            BANANA(1000, 1, 2)
            BANANA_STEW(10, 150, 300)
            MONKEY_BAR(20, 25, 50)
        }
    }
}
