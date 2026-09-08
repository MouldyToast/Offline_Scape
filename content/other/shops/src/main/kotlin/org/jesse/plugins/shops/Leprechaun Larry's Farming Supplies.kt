package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

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
