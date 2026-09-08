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

class AliSDiscountWares : ShopScript() {

    init {
        "Ali's Discount Wares"(122, ShopCurrency.COINS, CAN_SELL) {
            MENAPHITE_PURPLE_HAT(25, 21, 35)
            MENAPHITE_PURPLE_TOP(25, 12, 20)
            MENAPHITE_PURPLE_ROBE(25, 24, 40)
            MENAPHITE_PURPLE_KILT(25, 12, 20)
            MENAPHITE_RED_HAT(25, 21, 35)
            MENAPHITE_RED_TOP(25, 12, 20)
            MENAPHITE_RED_ROBE(25, 24, 40)
            MENAPHITE_RED_KILT(25, 12, 20)
        }
    }
}
