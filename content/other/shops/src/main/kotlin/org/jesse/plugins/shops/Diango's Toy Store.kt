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

class DiangoSToyStore : ShopScript() {

    init {
        "Diango's Toy Store"(149, ShopCurrency.COINS, STOCK_ONLY) {
            CHRONICLE(1, 0, 300)
            TELEPORT_CARD(100, 50, 150)
        }
    }
}
