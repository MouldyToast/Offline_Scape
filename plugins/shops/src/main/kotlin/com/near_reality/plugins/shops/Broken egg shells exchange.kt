package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class BrokenEggShellsExchange : ShopScript() {

    init {
        "Broken egg shells exchange"(369, ShopCurrency.BROKEN_EGG_SHELLS, NO_SELLING) {
            EASTER_MYSTERY_BOX(1_000_000, sellPrice = (-1), buyPrice = 200)
        }
    }
}
