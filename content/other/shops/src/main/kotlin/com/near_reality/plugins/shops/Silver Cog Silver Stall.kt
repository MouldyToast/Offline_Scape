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
import com.zenyte.game.item.ItemId.SILVER_BAR
import com.zenyte.game.item.ItemId.SILVER_ORE
import com.zenyte.game.item.ItemId.UNSTRUNG_SYMBOL

class SilverCogSilverStall : ShopScript() {

    init {
        "Silver Cog Silver Stall"(191, ShopCurrency.COINS, STOCK_ONLY) {
            UNSTRUNG_SYMBOL(2, 160, 300)
            SILVER_ORE(1, 60, 112)
            SILVER_BAR(1, 120, 225)
        }
    }
}
