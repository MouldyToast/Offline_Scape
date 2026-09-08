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

class ContrabandYakProduce : ShopScript() {

    init {
        "Contraband yak produce"(36, ShopCurrency.COINS, STOCK_ONLY) {
            YAKHIDE(25, 35, 55)
            RAW_YAK_MEAT(50, 1, 2)
            HAIR(50, 1, 2)
            CURED_YAKHIDE(10, 70, 110)
        }
    }
}
