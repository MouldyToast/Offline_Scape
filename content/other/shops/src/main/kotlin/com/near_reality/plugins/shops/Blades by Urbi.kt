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

class BladesByUrbi : ShopScript() {

    init {
        "Blades by Urbi"(139, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_DAGGER(1, 2, 15)
            IRON_DAGGER(1, 8, 52)
            STEEL_DAGGER(1, 31, 187)
            BLACK_DAGGER(0, 60, 360)
            MITHRIL_DAGGER(1, 81, 487)
            ADAMANT_DAGGER(1, 200, 1200)
            RUNE_DAGGER(1, 2000, 12000)
            DRAGON_DAGGER(0, 7500, 45000)
            BRONZE_SCIMITAR(1, 8, 48)
            IRON_SCIMITAR(1, 28, 168)
            STEEL_SCIMITAR(1, 100, 600)
        }
    }
}
