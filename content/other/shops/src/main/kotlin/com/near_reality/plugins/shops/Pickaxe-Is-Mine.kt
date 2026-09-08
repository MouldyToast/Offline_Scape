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
import com.zenyte.game.item.ItemId.ADAMANT_PICKAXE
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.MITHRIL_PICKAXE
import com.zenyte.game.item.ItemId.RUNE_PICKAXE
import com.zenyte.game.item.ItemId.STEEL_PICKAXE

class PickaxeIsMine : ShopScript() {

    init {
        "Pickaxe-Is-Mine"(190, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(6, 1, 1)
            STEEL_PICKAXE(4, 166, 650)
            MITHRIL_PICKAXE(3, 433, 1690)
            ADAMANT_PICKAXE(2, 1066, 4160)
            RUNE_PICKAXE(1, 10666, 41600)
        }
    }
}
