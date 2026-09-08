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

class AlryTheAnglerSAnglingAccessories : ShopScript() {

    init {
        "Alry The Angler's Angling Accessories"(293, ShopCurrency.MOLCH_PEARL, STOCK_ONLY) {
            PEARL_FISHING_ROD(1000, 0, 100)
            PEARL_FLY_FISHING_ROD(1000, 0, 120)
            PEARL_BARBARIAN_ROD(1000, 0, 150)
            FISH_SACK(1000, 0, 1000)
            ANGLER_HAT(1000, 0, 100)
            ANGLER_TOP(1000, 0, 100)
            ANGLER_WADERS(1000, 0, 100)
            ANGLER_BOOTS(1000, 0, 100)
        }
    }
}
