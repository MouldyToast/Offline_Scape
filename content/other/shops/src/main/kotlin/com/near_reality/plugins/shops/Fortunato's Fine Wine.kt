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
import com.zenyte.game.item.ItemId.BOTTLE_OF_WINE
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.JUG_OF_VINEGAR
import com.zenyte.game.item.ItemId.JUG_OF_WINE

class FortunatoSFineWine : ShopScript() {

    init {
        "Fortunato's Fine Wine"(151, ShopCurrency.COINS, STOCK_ONLY) {
            JUG_OF_WINE(50, 0, 1)
            JUG(3, 0, 1)
            EMPTY_JUG_PACK(5, -1, 140)
            BOTTLE_OF_WINE(2, 300, 500)
            JUG_OF_VINEGAR(500, -1, 1)
        }
    }
}
