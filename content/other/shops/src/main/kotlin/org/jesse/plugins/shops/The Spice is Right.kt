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

class TheSpiceIsRight : ShopScript() {

    init {
        "The Spice is Right"(143, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, 0, 1)
            GNOME_SPICE(10, 0, 3)
            CURRY_LEAF(0, 7, 28)
            PILE_OF_SALT(0, 5, 30)
            BUCKET_OF_SAP(0, 7, 45)
            ANTIPOISON3(20, 72, 432)
        }
    }
}
