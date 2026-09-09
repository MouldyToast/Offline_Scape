package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class LouieSArmouredLegsBazaar : ShopScript() {

    init {
        "Louie's Armoured Legs Bazaar"(125, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PLATELEGS(5, 52, 80)
            IRON_PLATELEGS(3, 182, 280)
            STEEL_PLATELEGS(2, 650, 1000)
            BLACK_PLATELEGS(1, 1248, 1920)
            MITHRIL_PLATELEGS(1, 1690, 2600)
            ADAMANT_PLATELEGS(1, 4160, 6400)
        }
    }
}
