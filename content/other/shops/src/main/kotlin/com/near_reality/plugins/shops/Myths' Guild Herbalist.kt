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
