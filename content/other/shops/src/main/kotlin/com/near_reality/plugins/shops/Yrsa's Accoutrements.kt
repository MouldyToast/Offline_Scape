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
import com.zenyte.game.item.ItemId.FREMENNIK_BLACK_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_BLUE_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_BLUE_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_BOOTS
import com.zenyte.game.item.ItemId.FREMENNIK_BROWN_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_BROWN_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_CYAN_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_GLOVES
import com.zenyte.game.item.ItemId.FREMENNIK_GREEN_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_GREY_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_GREY_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_HAT
import com.zenyte.game.item.ItemId.FREMENNIK_PINK_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_PURPLE_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_RED_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_RED_SHIRT
import com.zenyte.game.item.ItemId.FREMENNIK_ROBE
import com.zenyte.game.item.ItemId.FREMENNIK_SKIRT
import com.zenyte.game.item.ItemId.FREMENNIK_TEAL_CLOAK
import com.zenyte.game.item.ItemId.FREMENNIK_YELLOW_CLOAK

class YrsaSAccoutrements : ShopScript() {

    init {
        "Yrsa's Accoutrements"(54, ShopCurrency.COINS, STOCK_ONLY) {
            FREMENNIK_BLUE_SHIRT(5, 175, 325)
            FREMENNIK_RED_SHIRT(5, 175, 325)
            FREMENNIK_BROWN_SHIRT(5, 175, 325)
            FREMENNIK_GREY_SHIRT(5, 175, 325)
            FREMENNIK_BEIGE_SHIRT(5, 175, 325)
            FREMENNIK_ROBE(5, 390, 650)
            FREMENNIK_SKIRT(5, 390, 650)
            FREMENNIK_HAT(5, 390, 650)
            FREMENNIK_BOOTS(5, 390, 650)
            FREMENNIK_GLOVES(5, 390, 650)
            FREMENNIK_GREEN_CLOAK(5, 150, 325)
            FREMENNIK_BLUE_CLOAK(5, 150, 325)
            FREMENNIK_BROWN_CLOAK(5, 150, 325)
            FREMENNIK_CYAN_CLOAK(5, 150, 325)
            FREMENNIK_RED_CLOAK(5, 150, 325)
            FREMENNIK_GREY_CLOAK(5, 150, 325)
            FREMENNIK_YELLOW_CLOAK(5, 150, 325)
            FREMENNIK_TEAL_CLOAK(5, 150, 325)
            FREMENNIK_PURPLE_CLOAK(5, 150, 325)
            FREMENNIK_PINK_CLOAK(5, 150, 325)
            FREMENNIK_BLACK_CLOAK(5, 150, 325)
        }
    }
}
