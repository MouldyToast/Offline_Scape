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
import com.zenyte.game.item.ItemId.BODY_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE_PACK
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE_PACK
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.FIRE_RUNE_PACK
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MIND_RUNE_PACK
import com.zenyte.game.item.ItemId.WATER_RUNE
import com.zenyte.game.item.ItemId.WATER_RUNE_PACK

class ThyriaSWares : ShopScript() {

    init {
        "Thyria's Wares"(202, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(5000, 2, 4)
            WATER_RUNE(5000, 2, 4)
            AIR_RUNE(5000, 2, 4)
            EARTH_RUNE(5000, 2, 4)
            MIND_RUNE(5000, 1, 3)
            BODY_RUNE(5000, 1, 3)
            CHAOS_RUNE(250, -1, 90)
            DEATH_RUNE(150, -1, 267)
            FIRE_RUNE_PACK(80, -1, 430)
            WATER_RUNE_PACK(80, -1, 430)
            AIR_RUNE_PACK(80, -1, 430)
            EARTH_RUNE_PACK(80, -1, 430)
            MIND_RUNE_PACK(40, -1, 330)
            CHAOS_RUNE_PACK(35, -1, 9950)
        }
    }
}
