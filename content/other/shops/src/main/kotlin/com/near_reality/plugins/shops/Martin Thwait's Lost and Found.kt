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
import com.zenyte.game.item.ItemId.BRONZE_CLAWS
import com.zenyte.game.item.ItemId.BRONZE_KNIFE
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.IRON_CLAWS
import com.zenyte.game.item.ItemId.IRON_KNIFE
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.LOCKPICK
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.STEEL_CLAWS
import com.zenyte.game.item.ItemId.STEEL_KNIFE
import com.zenyte.game.item.ItemId.STETHOSCOPE

class MartinThwaitSLostAndFound : ShopScript() {

    init {
        "Martin Thwait's Lost and Found"(1, ShopCurrency.COINS, CAN_SELL, 0.6) {
            ROPE(50, 10, 18)
            LOCKPICK(25, 12, 20)
            CHISEL(30, 0, 1)
            KNIFE(20, 3, 6)
            STETHOSCOPE(25, 0, 10)
            BRONZE_KNIFE(15, 0, 1)
            IRON_KNIFE(10, 1, 3)
            STEEL_KNIFE(5, 6, 11)
            BRONZE_CLAWS(3, 9, 15)
            IRON_CLAWS(2, 30, 50)
            STEEL_CLAWS(1, 105, 175)
        }
    }
}
