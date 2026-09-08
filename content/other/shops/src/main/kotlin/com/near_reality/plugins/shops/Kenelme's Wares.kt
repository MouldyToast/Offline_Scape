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
import com.zenyte.game.item.ItemId.BANANA
import com.zenyte.game.item.ItemId.BREAD
import com.zenyte.game.item.ItemId.CABBAGE
import com.zenyte.game.item.ItemId.CHEESE
import com.zenyte.game.item.ItemId.CHOCOLATE_BAR
import com.zenyte.game.item.ItemId.POTATO
import com.zenyte.game.item.ItemId.POT_OF_FLOUR
import com.zenyte.game.item.ItemId.RAW_BEEF
import com.zenyte.game.item.ItemId.REDBERRIES
import com.zenyte.game.item.ItemId.TOMATO

class KenelmeSWares : ShopScript() {

    init {
        "Kenelme's Wares"(214, ShopCurrency.COINS, STOCK_ONLY) {
            POT_OF_FLOUR(3, 6, 10)
            RAW_BEEF(1, 0, 1)
            CABBAGE(3, 0, 1)
            BANANA(3, 1, 2)
            REDBERRIES(1, 1, 3)
            BREAD(0, 8, 12)
            CHOCOLATE_BAR(1, 6, 10)
            CHEESE(3, 2, 4)
            TOMATO(3, 2, 4)
            POTATO(1, 0, 1)
        }
    }
}
