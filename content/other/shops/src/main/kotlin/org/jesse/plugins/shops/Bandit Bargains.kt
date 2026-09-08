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

class BanditBargains : ShopScript() {

    init {
        "Bandit Bargains"(129, ShopCurrency.COINS, CAN_SELL) {
            WATERSKIN4(5, -1, 30)
            WATERSKIN0(5, -1, 15)
            JUG_OF_WATER(5, -1, 1)
            BOWL_OF_WATER(5, -1, 4)
            BUCKET_OF_WATER(5, -1, 6)
            JUG(5, -1, 1)
            EMPTY_JUG_PACK(8, -1, 140)
            BOWL(5, -1, 4)
            BUCKET(5, -1, 2)
            DESERT_BOOTS(5, -1, 20)
            DESERT_SHIRT(5, -1, 40)
            DESERT_ROBE(5, -1, 40)
            KNIFE(5, -1, 6)
        }
    }
}
