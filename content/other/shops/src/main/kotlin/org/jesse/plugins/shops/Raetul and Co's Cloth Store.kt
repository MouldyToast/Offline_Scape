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

class RaetulAndCoSClothStore : ShopScript() {

    init {
        "Raetul and Co's Cloth Store"(142, ShopCurrency.COINS, STOCK_ONLY) {
            LINEN(20, 22, 30)
            DESERT_SHIRT(20, 30, 40)
            DESERT_ROBE(20, 30, 40)
            DESERT_BOOTS(20, 15, 20)
            SILK(10, 22, 30)
            THREAD(50, 0, 1)
            NEEDLE(20, 0, 1)
        }
    }
}
