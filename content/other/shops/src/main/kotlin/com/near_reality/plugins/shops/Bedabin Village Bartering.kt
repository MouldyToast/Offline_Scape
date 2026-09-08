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

class BedabinVillageBartering : ShopScript() {

    init {
        "Bedabin Village Bartering"(131, ShopCurrency.COINS, CAN_SELL) {
            WATERSKIN4(5, -1, 36)
            WATERSKIN0(5, -1, 18)
            JUG_OF_WATER(5, -1, 1)
            BOWL_OF_WATER(5, -1, 4)
            BUCKET_OF_WATER(5, -1, 7)
            KNIFE(5, -1, 7)
            HAMMER(5, -1, 1)
        }
    }
}
