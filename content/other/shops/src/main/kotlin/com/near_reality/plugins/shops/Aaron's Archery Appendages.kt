package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ids.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

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
