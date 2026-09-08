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

class AaronSArcheryAppendages : ShopScript() {

    init {
        "Aaron's Archery Appendages"(64, ShopCurrency.COINS, STOCK_ONLY) {
            LEATHER_BODY(10, 10, 21)
            HARDLEATHER_BODY(10, 85, 170)
            STUDDED_BODY(10, 425, 850)
            LEATHER_CHAPS(20, 10, 20)
            STUDDED_CHAPS(15, 375, 750)
            COIF(10, 100, 200)
            LEATHER_COWL(10, 12, 24)
            LEATHER_VAMBRACES(10, 9, 18)
        }
    }
}
