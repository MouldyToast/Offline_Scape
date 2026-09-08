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
import com.zenyte.game.item.ItemId.FREMENNIK_BEIGE_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_BLUE_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_BROWN_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_GREY_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_RED_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_ROBE
import com.zenyte.game.item.ItemId.FREMENNIK_SKIRT
import com.zenyte.game.item.ItemId.SHIRT_5032
import com.zenyte.game.item.ItemId.SHIRT_5034
import com.zenyte.game.item.ItemId.SHORTS_5044
import com.zenyte.game.item.ItemId.SHORTS_5046
import com.zenyte.game.item.ItemId.SKIRT_5050
import com.zenyte.game.item.ItemId.SKIRT_5052
import com.zenyte.game.item.ItemId.TROUSERS_5038
import com.zenyte.game.item.ItemId.TROUSERS_5040
import com.zenyte.game.item.ItemId.WOVEN_TOP_5026
import com.zenyte.game.item.ItemId.WOVEN_TOP_5028

class MiscellanianClothesShop : ShopScript() {

    init {
        "Miscellanian Clothes Shop"(48, ShopCurrency.COINS, STOCK_ONLY) {
            FREMENNIK_BROWN_SHIRT(5, 137, 250)
            FREMENNIK_GREY_SHIRT(5, 137, 250)
            FREMENNIK_BEIGE_SHIRT(5, 137, 250)
            FREMENNIK_RED_SHIRT(5, 137, 250)
            FREMENNIK_BLUE_SHIRT(5, 137, 250)
            FREMENNIK_ROBE(5, 275, 500)
            FREMENNIK_SKIRT(5, 275, 500)
            SKIRT_5050(3, 302, 550)
            SKIRT_5052(3, 343, 625)
            TROUSERS_5038(3, 385, 700)
            TROUSERS_5040(3, 412, 750)
            SHORTS_5044(3, 198, 360)
            SHORTS_5046(3, 214, 390)
            WOVEN_TOP_5026(3, 343, 625)
            WOVEN_TOP_5028(3, 357, 650)
            SHIRT_5032(3, 330, 600)
            SHIRT_5034(3, 343, 625)
        }
    }
}
