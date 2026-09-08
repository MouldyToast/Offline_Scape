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

class ThessaliaSFineClothes : ShopScript() {

    init {
        "Thessalia's Fine Clothes"(165, ShopCurrency.COINS, STOCK_ONLY) {
            WHITE_APRON(3, 1, 2)
            LEATHER_BODY(12, 11, 21)
            LEATHER_GLOVES(10, 3, 6)
            LEATHER_BOOTS(10, 3, 6)
            BROWN_APRON(1, 1, 2)
            PINK_SKIRT(5, 1, 2)
            BLACK_SKIRT(3, 1, 2)
            BLUE_SKIRT(2, 1, 2)
            RED_CAPE(4, 1, 2)
            SILK(5, 16, 30)
            PRIEST_GOWN_428(3, 2, 5)
            PRIEST_GOWN(3, 2, 5)
        }
    }
}
