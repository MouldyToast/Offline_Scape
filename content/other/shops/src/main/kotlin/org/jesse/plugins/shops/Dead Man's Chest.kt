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

class DeadManSChest : ShopScript() {

    init {
        "Dead Man's Chest"(102, ShopCurrency.COINS, STOCK_ONLY) {
            GROG(10, 1, 3)
            KARAMJAN_RUM(10, 9, 27)
        }
    }
}
