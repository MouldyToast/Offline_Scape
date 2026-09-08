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

class NurmofSPickaxeShop : ShopScript() {

    init {
        "Nurmof's Pickaxe Shop"(7, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(6, 0, 1)
            IRON_PICKAXE(5, 33, 140)
            STEEL_PICKAXE(4, 212, 500)
            MITHRIL_PICKAXE(3, 780, 1300)
            ADAMANT_PICKAXE(2, 1920, 3200)
            RUNE_PICKAXE(1, 19200, 32000)
        }
    }
}
