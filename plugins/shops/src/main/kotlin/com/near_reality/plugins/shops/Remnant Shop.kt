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

class RemnantShop : ShopScript() {

    init {
        "Remnant Shop"(6969, EXCHANGE_POINTS, STOCK_ONLY) {
            REMNANT_POINT_VOUCHER_1(100_000_000, 1, 1)
            ORB_OF_XERIC(1000, 650, 1000)
            ORB_OF_BLOOD(1000, 650, 1000)
            WORLD_BOOST_TOKEN(1000, (-1), 6500)
            OVERLOAD_4(1000, (-1), 200)

            SAGES_GREAVES(1, -1, 30_000)

            PET_POSTIE_PETE(1000, 1.unaryMinus(), 15_000)
            PET_SHADOW_ARCHER(1000, 1.unaryMinus(), 15_000)
            PET_SHADOW_WIZARD(1000, 1.unaryMinus(), 15_000)
            PET_SHADOW_WARRIOR(1000, 1.unaryMinus(), 15_000)
            PET_TOUCAN(1000, 1.unaryMinus(), 15_000)
            PET_KING_PENGUIN(1000, 1.unaryMinus(), 20_000)
            PET_IMP(1000, 1.unaryMinus(), 20_000)
            PET_KKLIK(1000, 1.unaryMinus(), 20_000)
            PET_HEALER_DEATH_SPAWN(1000, 1.unaryMinus(), 25_000)
            PET_HOLY_DEATH_SPAWN(1000, 1.unaryMinus(), 25_000)
            PET_SEREN(1000, 1.unaryMinus(), 35_000)
            PET_CORRUPT_BEAST(1000, 1.unaryMinus(), 35_000)
            PET_ROC(1000, 1.unaryMinus(), 55_000)
            PET_KRATOS(1000, (1).unaryMinus(), 175_000)
        }
    }
}
