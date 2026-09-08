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

class ArdougneSpiceStall : ShopScript() {

    init {
        "Ardougne Spice Stall"(82, ShopCurrency.COINS, STOCK_ONLY) {
            SPICE(1, 138, 230)
            KNIFE(1, -1, 6)
            GARLIC(1000, 1, 3, 1)
        }
    }
}
