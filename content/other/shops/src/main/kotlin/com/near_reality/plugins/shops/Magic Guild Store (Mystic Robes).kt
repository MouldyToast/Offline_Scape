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
import com.zenyte.game.item.ItemId.MYSTIC_BOOTS
import com.zenyte.game.item.ItemId.MYSTIC_GLOVES
import com.zenyte.game.item.ItemId.MYSTIC_HAT
import com.zenyte.game.item.ItemId.MYSTIC_ROBE_BOTTOM
import com.zenyte.game.item.ItemId.MYSTIC_ROBE_TOP

class MagicGuildStoreMysticRobes : ShopScript() {

    init {
        "Magic Guild Store (Mystic Robes)"(70, ShopCurrency.COINS, STOCK_ONLY) {
            MYSTIC_HAT(100, 3600, 9000)
            MYSTIC_ROBE_TOP(100, 28800, 72000)
            MYSTIC_ROBE_BOTTOM(100, 19200, 48000)
            MYSTIC_GLOVES(100, 2400, 6000)
            MYSTIC_BOOTS(100, 2400, 6000)
        }
    }
}
