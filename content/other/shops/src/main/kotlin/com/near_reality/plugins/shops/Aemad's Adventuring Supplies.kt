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

class AemadSAdventuringSupplies : ShopScript() {

    init {
        "Aemad's Adventuring Supplies"(76, ShopCurrency.COINS, CAN_SELL) {
            VIAL_OF_WATER(500, 0, 2)
            WATERFILLED_VIAL_PACK(250, 80, 261)
            BRONZE_PICKAXE(2, 0, 1)
            IRON_AXE(2, 22, 72)
            COOKED_MEAT(2, 1, 5)
            TINDERBOX(2, 0, 1)
            BALL_OF_WOOL(30, 0, 2)
            BRONZE_ARROW(500, 0, 1)
            ROPE(20, 7, 23)
            PAPYRUS(50, 4, 13)
            KNIFE(2, 2, 7)
        }
    }
}
