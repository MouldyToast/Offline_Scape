package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class AgmundiQualityClothes : ShopScript() {

    init {
        "Agmundi Quality Clothes"(194, ShopCurrency.COINS, STOCK_ONLY) {
            SKIRT_5050(3, 302, 715)
            SKIRT_5052(3, 343, 812)
            TROUSERS_5038(3, 385, 910)
            TROUSERS_5040(3, 412, 975)
            SHORTS_5044(3, 198, 468)
            SHORTS_5046(3, 214, 507)
            WOVEN_TOP_5026(3, 343, 812)
            WOVEN_TOP_5028(3, 357, 845)
            SHIRT_5032(3, 330, 780)
            SHIRT_5034(3, 343, 812)
        }
    }
}
