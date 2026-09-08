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

class DalSGeneralOgreSupplies : ShopScript() {

    init {
        "Dal's General Ogre Supplies"(26, ShopCurrency.COINS, STOCK_ONLY) {
            POT(30, 1, 1)
            JUG(10, 1, 1)
            KNIFE(10, 7, 25)
            BUCKET(30, 1, 2)
            TINDERBOX(10, 1, 1)
            CHISEL(10, 4, 14)
            HAMMER(10, 3, 13)
        }
    }
}
