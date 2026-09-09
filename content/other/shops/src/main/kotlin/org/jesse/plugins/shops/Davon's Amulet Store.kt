package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class DavonSAmuletStore : ShopScript() {

    init {
        "Davon's Amulet Store"(101, ShopCurrency.COINS, STOCK_ONLY, Shop.DEFAULT_SELL_MULTIPLIER, ShopDiscount.KARAMJA_DIARY) {
            HOLY_SYMBOL(0, 225, 360)
            AMULET_OF_MAGIC(0, 675, 1080)
            AMULET_OF_DEFENCE(0, 956, 1530)
            AMULET_OF_STRENGTH(0, 1215, 2430)
            AMULET_OF_POWER(0, 2643, 4230)
        }
    }
}
