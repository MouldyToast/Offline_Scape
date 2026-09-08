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
import com.zenyte.game.item.ItemId.EMPTY_VIAL_PACK
import com.zenyte.game.item.ItemId.EYE_OF_NEWT
import com.zenyte.game.item.ItemId.EYE_OF_NEWT_PACK
import com.zenyte.game.item.ItemId.PESTLE_AND_MORTAR
import com.zenyte.game.item.ItemId.VIAL

class JatixSHerbloreShop : ShopScript() {

    init {
        "Jatix's Herblore Shop"(23, ShopCurrency.COINS, STOCK_ONLY) {
            VIAL(800, 1, 2)
            EMPTY_VIAL_PACK(800, 140, 200)
            PESTLE_AND_MORTAR(3, 2, 4)
            EYE_OF_NEWT(800, 2, 3)
            EYE_OF_NEWT_PACK(100, 210, 300)
        }
    }
}
