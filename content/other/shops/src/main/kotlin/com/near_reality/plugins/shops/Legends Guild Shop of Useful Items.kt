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
import com.zenyte.game.item.ItemId.CAPE_OF_LEGENDS
import com.zenyte.game.item.ItemId.DUSTY_KEY
import com.zenyte.game.item.ItemId.MAZE_KEY
import com.zenyte.game.item.ItemId.SHIELD_RIGHT_HALF

class LegendsGuildShopOfUsefulItems : ShopScript() {

    init {
        "Legends Guild Shop of Useful Items"(61, ShopCurrency.COINS, STOCK_ONLY) {
            DUSTY_KEY(5, 0, 1)
            MAZE_KEY(3, 0, 1)
            SHIELD_RIGHT_HALF(1, 250000, 750000)
            CAPE_OF_LEGENDS(3, 0, 675)
        }
    }
}
