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
import com.zenyte.game.item.ItemId.BASKET
import com.zenyte.game.item.ItemId.BASKET_PACK
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.COMPOST
import com.zenyte.game.item.ItemId.COMPOST_PACK
import com.zenyte.game.item.ItemId.EMPTY_SACK
import com.zenyte.game.item.ItemId.FILLED_PLANT_POT
import com.zenyte.game.item.ItemId.GARDENING_TROWEL
import com.zenyte.game.item.ItemId.PLANT_CURE
import com.zenyte.game.item.ItemId.PLANT_POT_PACK
import com.zenyte.game.item.ItemId.RAKE
import com.zenyte.game.item.ItemId.SACK_PACK
import com.zenyte.game.item.ItemId.SECATEURS
import com.zenyte.game.item.ItemId.SEED_DIBBER
import com.zenyte.game.item.ItemId.SPADE
import com.zenyte.game.item.ItemId.WATERING_CAN

class VannahSFarmStore : ShopScript() {

    init {
        "Vannah's Farm Store"(208, ShopCurrency.COINS, STOCK_ONLY) {
            RAKE(5, 4, 6)
            SEED_DIBBER(5, 4, 6)
            SECATEURS(5, 3, 5)
            SPADE(5, 2, 3)
            GARDENING_TROWEL(5, 8, 12)
            WATERING_CAN(5, 5, 8)
            FILLED_PLANT_POT(100, 0, 1)
            PLANT_POT_PACK(10, 70, 100)
            COMPOST(100, 14, 20)
            COMPOST_PACK(10, 1400, 2000)
            BUCKET(100, 0, 1)
            EMPTY_SACK(100, 0, 1)
            SACK_PACK(30, 70, 100)
            BASKET(100, 0, 1)
            BASKET_PACK(30, 70, 100)
            PLANT_CURE(40, 28, 40)
        }
    }
}
