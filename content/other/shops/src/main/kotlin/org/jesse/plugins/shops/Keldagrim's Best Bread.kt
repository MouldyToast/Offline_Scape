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

class KeldagrimSBestBread : ShopScript() {

    init {
        "Keldagrim's Best Bread"(187, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(10, 9, 18)
            CAKE(3, 40, 75)
            CHOCOLATE_SLICE(8, 24, 45)
        }
    }
}
