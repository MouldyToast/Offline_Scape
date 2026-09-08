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

class PollnivneachGeneralStore : ShopScript() {

    init {
        "Pollnivneach general store"(137, ShopCurrency.COINS, CAN_SELL) {
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 140)
            WATERSKIN3(20, 9, 27)
            DESERT_SHIRT(3, 13, 40)
            DESERT_BOOTS(2, 6, 20)
            BUCKET(12, 1, 2)
            FAKE_BEARD(11, 0, 1)
            KHARIDIAN_HEADPIECE(12, 0, 1)
            CHEESE(5, -1, 4)
            LIME(5, -1, 2)
            TOMATO(5, -1, 4)
            JUG_OF_WATER(5, 1, 1)
            BOWL_OF_WATER(7, 1, 4)
            BUCKET_OF_WATER(8, 2, 6)
        }
    }
}
