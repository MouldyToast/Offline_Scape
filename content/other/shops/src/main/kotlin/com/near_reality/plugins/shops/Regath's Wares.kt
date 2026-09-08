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
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.AIR_RUNE_PACK
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.BLUE_WIZARD_HAT
import com.zenyte.game.item.ItemId.BODY_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE_PACK
import com.zenyte.game.item.ItemId.EYE_OF_NEWT
import com.zenyte.game.item.ItemId.EYE_OF_NEWT_PACK
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.FIRE_RUNE_PACK
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MIND_RUNE_PACK
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.SOUL_RUNE
import com.zenyte.game.item.ItemId.VIAL_OF_WATER
import com.zenyte.game.item.ItemId.WATERFILLED_VIAL_PACK
import com.zenyte.game.item.ItemId.WATER_RUNE
import com.zenyte.game.item.ItemId.WATER_RUNE_PACK
import com.zenyte.game.item.ItemId.WIZARD_HAT

class RegathSWares : ShopScript() {

    init {
        "Regath's Wares"(201, ShopCurrency.COINS, STOCK_ONLY) {
            VIAL_OF_WATER(30, -1, 2)
            WATERFILLED_VIAL_PACK(25, -1, 281)
            ROPE(10, -1, 25)
            FIRE_RUNE(500, 2, 5)
            WATER_RUNE(500, 2, 5)
            AIR_RUNE(500, 2, 5)
            EARTH_RUNE(500, 2, 5)
            MIND_RUNE(300, 1, 4)
            BODY_RUNE(300, 1, 4)
            BLOOD_RUNE(50, -1, 560)
            SOUL_RUNE(50, -1, 420)
            FIRE_RUNE_PACK(80, -1, 602)
            WATER_RUNE_PACK(80, -1, 602)
            AIR_RUNE_PACK(80, -1, 602)
            EARTH_RUNE_PACK(80, -1, 602)
            MIND_RUNE_PACK(40, -1, 462)
            BLUE_WIZARD_HAT(1, 1, 2)
            WIZARD_HAT(1, 1, 2)
            EYE_OF_NEWT(300, 1, 4)
            EYE_OF_NEWT_PACK(60, -1, 420)
        }
    }
}
