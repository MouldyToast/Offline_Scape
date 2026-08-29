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

class AfkSkillingShop : ShopScript() {

    init {
        "AFK Skilling Shop"(292, AFK_POINTS, NO_SELLING) {
        	32240(1000, -1, 756_000) // Sleeping Cap (afk)
        	IMCANDO_HAMMER(100, -1, 220_000)
        	GRICOLLERS_CAN(100, -1, 110_000)
        	COAL_BAG(100, -1, 110_000)
        	3050(1000, -1, 225)
        	6694(1000, -1, 325)
        	1516(1000, -1, 110)
        	1618(1000, -1, 180)
        	2354(1000, -1, 150)
        	22936(1000, -1, 225)
        	1938(1000, -1, 110)
        	1988(1000, -1, 110)
        	25110(1000, -1, 500_000) // Echo Axe
        	25112(1000, -1, 500_000) // Echo Pickaxe
        	25114(1000, -1, 500_000) // Echo Harpoon
        	28771(1000, -1, 500_000) // Sage's Greves
        }
    }
}
