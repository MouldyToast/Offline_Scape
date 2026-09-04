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

class CulinaromancerSChest : ShopScript() {

    init {
        "Culinaromancer's Chest"(158, ShopCurrency.COINS, STOCK_ONLY) {
            HARDLEATHER_GLOVES(10, 0, 65)
            BRONZE_GLOVES(10, 0, 130)
            IRON_GLOVES(10, 0, 325)
            STEEL_GLOVES(10, 0, 650)
            BLACK_GLOVES(10, 0, 1300)
            MITHRIL_GLOVES(10, 0, 1950)
            ADAMANT_GLOVES(10, 0, 3250)
            RUNE_GLOVES(10, 0, 6500)
            DRAGON_GLOVES(10, 0, 130000)
            BARROWS_GLOVES(10, 0, 130000)
            WOODEN_SPOON(10, 0, 45)
            EGG_WHISK(10, 0, 65)
            SPORK(10, 0, 422)
            SPATULA(10, 0, 2496)
            FRYING_PAN(10, 0, 2158)
            SKEWER(10, 0, 4160)
            ROLLING_PIN(10, 0, 18720)
            KITCHEN_KNIFE(10, 0, 10400)
            MEAT_TENDERISER(10, 0, 53950)
            CLEAVER(10, 0, 33280)
        }
    }
}
