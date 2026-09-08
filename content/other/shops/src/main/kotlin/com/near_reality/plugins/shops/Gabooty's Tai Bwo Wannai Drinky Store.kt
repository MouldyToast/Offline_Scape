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
import com.zenyte.game.item.ItemId.BLURBERRY_SPECIAL
import com.zenyte.game.item.ItemId.CHOC_SATURDAY
import com.zenyte.game.item.ItemId.DRUNK_DRAGON
import com.zenyte.game.item.ItemId.FRUIT_BLAST
import com.zenyte.game.item.ItemId.PINEAPPLE_PUNCH
import com.zenyte.game.item.ItemId.WIZARD_BLIZZARD

class GabootySTaiBwoWannaiDrinkyStore : ShopScript() {

    init {
        "Gabooty's Tai Bwo Wannai Drinky Store"(110, ShopCurrency.COINS, STOCK_ONLY) {
            FRUIT_BLAST(0, 10, 30)
            DRUNK_DRAGON(0, 10, 30)
            PINEAPPLE_PUNCH(0, 10, 30)
            WIZARD_BLIZZARD(0, 10, 30)
            BLURBERRY_SPECIAL(0, 10, 30)
            CHOC_SATURDAY(0, 10, 30)
        }
    }
}
