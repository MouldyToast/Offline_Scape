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
import com.zenyte.game.item.ItemId.ANTIPOISON3
import com.zenyte.game.item.ItemId.BUCKET_OF_SAP
import com.zenyte.game.item.ItemId.CURRY_LEAF
import com.zenyte.game.item.ItemId.GNOME_SPICE
import com.zenyte.game.item.ItemId.PILE_OF_SALT
import com.zenyte.game.item.ItemId.POT

class TheSpiceIsRight : ShopScript() {

    init {
        "The Spice is Right"(143, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, 0, 1)
            GNOME_SPICE(10, 0, 3)
            CURRY_LEAF(0, 7, 28)
            PILE_OF_SALT(0, 5, 30)
            BUCKET_OF_SAP(0, 7, 45)
            ANTIPOISON3(20, 72, 432)
        }
    }
}
