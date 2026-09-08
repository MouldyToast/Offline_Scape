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

class YeOldeTeaShoppe : ShopScript() {

    init {
        "Ye Olde Tea Shoppe"(168, ShopCurrency.COINS, STOCK_ONLY) {
            CUP_OF_TEA(20, 6, 10)
        }
    }
}
