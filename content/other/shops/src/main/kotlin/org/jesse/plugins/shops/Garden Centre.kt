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

class GardenCentre : ShopScript() {

    init {
        "Garden Centre"(11, ShopCurrency.COINS, STOCK_ONLY) {
            BAGGED_DEAD_TREE(20, 400, 1000)
            BAGGED_NICE_TREE(20, 800, 2000)
            BAGGED_OAK_TREE(20, 2000, 5000)
            BAGGED_WILLOW_TREE(20, 4000, 10000)
            BAGGED_MAPLE_TREE(20, 6000, 15000)
            BAGGED_YEW_TREE(20, 8000, 20000)
            BAGGED_MAGIC_TREE(20, 20000, 50000)
            BAGGED_PLANT_1(20, 400, 1000)
            BAGGED_PLANT_2(20, 2000, 5000)
            BAGGED_PLANT_3(20, 4000, 10000)
            THORNY_HEDGE(20, 2000, 5000)
            NICE_HEDGE(20, 4000, 10000)
            SMALL_BOX_HEDGE(20, 6000, 15000)
            TOPIARY_HEDGE(20, 8000, 20000)
            FANCY_HEDGE(20, 10000, 25000)
            TALL_FANCY_HEDGE(20, 20000, 50000)
            TALL_BOX_HEDGE(20, 40000, 100000)
            BAGGED_FLOWER(20, 2000, 5000)
            BAGGED_DAFFODILS(20, 4000, 10000)
            BAGGED_BLUEBELLS(20, 6000, 15000)
            BAGGED_SUNFLOWER(20, 2000, 5000)
            BAGGED_MARIGOLDS(20, 4000, 10000)
            BAGGED_ROSES(20, 6000, 15000)
        }
    }
}
