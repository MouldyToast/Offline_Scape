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

class SirTiffyCashienArmouryShop : ShopScript() {

    init {
        "Initiate & Proselyte Armour"(1006, ShopCurrency.COINS, STOCK_ONLY) {
            INITIATE_SALLET(2000, 2400, 6000)
            INITIATE_HAUBERK(2000, 4000, 10000)
            INITIATE_CUISSE(2000, 3200, 8000)
            PROSELYTE_SALLET(2000, 3200, 8000)
            PROSELYTE_HAUBERK(2000, 4800, 12000)
            PROSELYTE_CUISSE(2000, 4000, 10000)
            PROSELYTE_TASSET(2000, 4000, 10000)
        }
    }
}
