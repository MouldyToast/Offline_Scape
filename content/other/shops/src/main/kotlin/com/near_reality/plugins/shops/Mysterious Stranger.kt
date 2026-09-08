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
import com.zenyte.game.item.ItemId.VERZIKS_CRYSTAL_SHARD

class MysteriousStranger : ShopScript() {

    init {
        "Mysterious Stranger"(555, ShopCurrency.COINS, STOCK_ONLY) {
            VERZIKS_CRYSTAL_SHARD(9500, 10000, 75000)
        }
    }
}
