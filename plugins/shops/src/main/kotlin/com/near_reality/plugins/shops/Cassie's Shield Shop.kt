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

class CassieSShieldShop : ShopScript() {

    init {
        "Cassie's Shield Shop"(10, ShopCurrency.COINS, STOCK_ONLY) {
            WOODEN_SHIELD(5, 12, 20)
            BRONZE_SQ_SHIELD(3, 28, 48)
            BRONZE_KITESHIELD(3, 40, 68)
            IRON_SQ_SHIELD(2, 100, 168)
            IRON_KITESHIELD(0, 142, 223)
            STEEL_SQ_SHIELD(0, 360, 600)
            STEEL_KITESHIELD(0, 510, 850)
            MITHRIL_SQ_SHIELD(0, 936, 1560)
        }
    }
}
