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
import com.zenyte.game.item.ItemId.CITIZEN_SHOES
import com.zenyte.game.item.ItemId.CITIZEN_TOP
import com.zenyte.game.item.ItemId.CITIZEN_TROUSERS
import com.zenyte.game.item.ItemId.VYREWATCH_LEGS
import com.zenyte.game.item.ItemId.VYREWATCH_SHOES
import com.zenyte.game.item.ItemId.VYREWATCH_TOP

class TraderSvenSBlackMarketGoods : ShopScript() {

    init {
        "Trader Sven's Black-market Goods"(173, ShopCurrency.COINS, STOCK_ONLY) {
            CITIZEN_TOP(10, 2, 6)
            CITIZEN_TROUSERS(10, 2, 6)
            CITIZEN_SHOES(10, 2, 6)
            VYREWATCH_TOP(10, 260, 650)
            VYREWATCH_LEGS(10, 260, 650)
            VYREWATCH_SHOES(10, 260, 650)
        }
    }
}
