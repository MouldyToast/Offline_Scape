package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

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
