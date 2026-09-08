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

class RokSChocsBox : ShopScript() {

    init {
        "Rok's Chocs Box"(135, ShopCurrency.COINS, STOCK_ONLY) {
            CHOCICE(30, 19, 30)
            CHOCOLATE_BAR(25, 6, 10)
        }
    }
}
