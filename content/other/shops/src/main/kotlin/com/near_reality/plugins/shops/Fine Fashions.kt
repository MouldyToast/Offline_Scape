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
import com.zenyte.game.item.ItemId.BLUE_BOOTS
import com.zenyte.game.item.ItemId.BLUE_HAT
import com.zenyte.game.item.ItemId.BLUE_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.BLUE_ROBE_TOP
import com.zenyte.game.item.ItemId.CREAM_BOOTS
import com.zenyte.game.item.ItemId.CREAM_HAT
import com.zenyte.game.item.ItemId.CREAM_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.CREAM_ROBE_TOP
import com.zenyte.game.item.ItemId.GREEN_BOOTS
import com.zenyte.game.item.ItemId.GREEN_HAT
import com.zenyte.game.item.ItemId.GREEN_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.GREEN_ROBE_TOP
import com.zenyte.game.item.ItemId.PINK_BOOTS
import com.zenyte.game.item.ItemId.PINK_HAT
import com.zenyte.game.item.ItemId.PINK_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.PINK_ROBE_TOP
import com.zenyte.game.item.ItemId.TURQUOISE_BOOTS
import com.zenyte.game.item.ItemId.TURQUOISE_HAT
import com.zenyte.game.item.ItemId.TURQUOISE_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.TURQUOISE_ROBE_TOP

class FineFashions : ShopScript() {

    init {
        "Fine Fashions"(92, ShopCurrency.COINS, STOCK_ONLY) {
            PINK_HAT(5, 88, 160)
            GREEN_HAT(5, 88, 160)
            BLUE_HAT(5, 88, 160)
            CREAM_HAT(5, 88, 160)
            TURQUOISE_HAT(5, 88, 160)
            PINK_ROBE_TOP(5, 99, 180)
            GREEN_ROBE_TOP(5, 99, 180)
            BLUE_ROBE_TOP(5, 99, 180)
            CREAM_ROBE_TOP(5, 99, 180)
            TURQUOISE_ROBE_TOP(5, 99, 180)
            PINK_ROBE_BOTTOMS(5, 99, 180)
            GREEN_ROBE_BOTTOMS(5, 99, 180)
            BLUE_ROBE_BOTTOMS(5, 99, 180)
            CREAM_ROBE_BOTTOMS(5, 99, 180)
            TURQUOISE_ROBE_BOTTOMS(5, 99, 180)
            PINK_BOOTS(5, 110, 200)
            GREEN_BOOTS(5, 110, 200)
            BLUE_BOOTS(5, 110, 200)
            CREAM_BOOTS(5, 110, 200)
            TURQUOISE_BOOTS(5, 110, 200)
        }
    }
}
