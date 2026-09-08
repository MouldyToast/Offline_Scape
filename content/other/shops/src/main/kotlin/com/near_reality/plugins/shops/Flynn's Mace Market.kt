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
import com.zenyte.game.item.ItemId.ADAMANT_MACE
import com.zenyte.game.item.ItemId.BRONZE_MACE
import com.zenyte.game.item.ItemId.IRON_MACE
import com.zenyte.game.item.ItemId.MITHRIL_MACE
import com.zenyte.game.item.ItemId.STEEL_MACE

class FlynnSMaceMarket : ShopScript() {

    init {
        "Flynn's Mace Market"(12, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_MACE(5, 10, 18)
            IRON_MACE(4, 37, 63)
            STEEL_MACE(4, 135, 225)
            MITHRIL_MACE(3, 351, 585)
            ADAMANT_MACE(2, 864, 1440)
        }
    }
}
