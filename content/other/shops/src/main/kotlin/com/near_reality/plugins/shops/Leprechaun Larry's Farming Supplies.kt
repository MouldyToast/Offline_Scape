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
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.COCKTAIL_GLASS
import com.zenyte.game.item.ItemId.GARDENING_TROWEL
import com.zenyte.game.item.ItemId.PLANT_CURE
import com.zenyte.game.item.ItemId.RAKE
import com.zenyte.game.item.ItemId.ROCK_1480
import com.zenyte.game.item.ItemId.SECATEURS
import com.zenyte.game.item.ItemId.SEED_DIBBER
import com.zenyte.game.item.ItemId.SPADE
import com.zenyte.game.item.ItemId.WATERING_CAN

class LeprechaunLarrySFarmingSupplies : ShopScript() {

    init {
        "Leprechaun Larry's Farming Supplies"(199, ShopCurrency.COINS, STOCK_ONLY) {
            RAKE(6, 3, 6)
            SEED_DIBBER(4, 3, 6)
            SECATEURS(1, 3, 5)
            SPADE(3, 1, 3)
            GARDENING_TROWEL(2, 7, 12)
            WATERING_CAN(2, 4, 8)
            BUCKET(2, 2, 20)
            PLANT_CURE(5, 24, 40)
            COCKTAIL_GLASS(10, 1, 1)
            ROCK_1480(3, 0, 1)
        }
    }
}
