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

class FlosiSFishmongers : ShopScript() {

    init {
        "Flosi's Fishmongers"(37, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_LOBSTER(5, 105, 165)
            RAW_TUNA(20, 70, 110)
            RAW_SALMON(20, 35, 55)
            RAW_COD(20, 17, 27)
            RAW_SHARK(0, 210, 330)
        }
    }
}
