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

class MythsGuildHerbalist : ShopScript() {

    init {
        "Myths' Guild Herbalist"(30, ShopCurrency.COINS, STOCK_ONLY) {
            PESTLE_AND_MORTAR(5, 2, 4)
            VIAL(50, 1, 2)
            EMPTY_VIAL_PACK(10, 140, 200)
            EYE_OF_NEWT(50, 2, 3)
            EYE_OF_NEWT_PACK(20, 210, 300)
        }
    }
}
