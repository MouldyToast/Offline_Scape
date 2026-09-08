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

class BattleRunes : ShopScript() {

    init {
        "Battle Runes"(238, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(100, 2, 4)
            WATER_RUNE(100, 2, 4)
            AIR_RUNE(100, 2, 4)
            EARTH_RUNE(100, 2, 4)
            MIND_RUNE(100, 1, 3)
            BODY_RUNE(100, 1, 3)
            CHAOS_RUNE(50, 54, 90)
            DEATH_RUNE(50, 108, 180)
            FIRE_RUNE_PACK(35, 258, 430)
            WATER_RUNE_PACK(35, 258, 430)
            AIR_RUNE_PACK(35, 258, 430)
            EARTH_RUNE_PACK(35, 258, 430)
            MIND_RUNE_PACK(25, 198, 330)
            CHAOS_RUNE_PACK(25, 5970, 9950)
        }
    }
}
