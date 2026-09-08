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

class RufusSMeatEmporium : ShopScript() {

    init {
        "Rufus's Meat Emporium"(172, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_BEEF(10, 0, 1)
            RAW_CHICKEN(10, 0, 1)
            RAW_RAT_MEAT(10, 0, 1)
            RAW_BEAR_MEAT(10, 0, 1)
            RAW_TROUT(5, 14, 26)
            RAW_PIKE(5, 17, 32)
            RAW_SALMON(5, 35, 65)
            RAW_SHARK(1, 210, 390)
        }
    }
}
