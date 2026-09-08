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
import com.zenyte.game.item.ItemId.GOUT_TUBER
import com.zenyte.game.item.ItemId.JADE
import com.zenyte.game.item.ItemId.JADE_MACHETE
import com.zenyte.game.item.ItemId.OPAL
import com.zenyte.game.item.ItemId.OPAL_MACHETE
import com.zenyte.game.item.ItemId.RED_TOPAZ
import com.zenyte.game.item.ItemId.RED_TOPAZ_MACHETE
import com.zenyte.game.item.ItemId.TRIBAL_TOP
import com.zenyte.game.item.ItemId.TRIBAL_TOP_6351
import com.zenyte.game.item.ItemId.TRIBAL_TOP_6361
import com.zenyte.game.item.ItemId.TRIBAL_TOP_6371
import com.zenyte.game.item.ItemId.UNCUT_JADE
import com.zenyte.game.item.ItemId.UNCUT_OPAL
import com.zenyte.game.item.ItemId.UNCUT_RED_TOPAZ
import com.zenyte.game.item.ItemId.VILLAGER_ARMBAND
import com.zenyte.game.item.ItemId.VILLAGER_ARMBAND_6359
import com.zenyte.game.item.ItemId.VILLAGER_ARMBAND_6369
import com.zenyte.game.item.ItemId.VILLAGER_ARMBAND_6379
import com.zenyte.game.item.ItemId.VILLAGER_HAT
import com.zenyte.game.item.ItemId.VILLAGER_HAT_6355
import com.zenyte.game.item.ItemId.VILLAGER_HAT_6365
import com.zenyte.game.item.ItemId.VILLAGER_HAT_6375
import com.zenyte.game.item.ItemId.VILLAGER_ROBE
import com.zenyte.game.item.ItemId.VILLAGER_ROBE_6353
import com.zenyte.game.item.ItemId.VILLAGER_ROBE_6363
import com.zenyte.game.item.ItemId.VILLAGER_ROBE_6373
import com.zenyte.game.item.ItemId.VILLAGER_SANDALS
import com.zenyte.game.item.ItemId.VILLAGER_SANDALS_6357
import com.zenyte.game.item.ItemId.VILLAGER_SANDALS_6367
import com.zenyte.game.item.ItemId.VILLAGER_SANDALS_6377

class GabootySTaiBwoWannaiCooperative : ShopScript() {

    init {
        "Gabooty's Tai Bwo Wannai Cooperative"(109, ShopCurrency.COINS, STOCK_ONLY) {
            TRIBAL_TOP(50, 100, -1)
            VILLAGER_ROBE(50, 83, -1)
            VILLAGER_HAT(50, 66, -1)
            VILLAGER_SANDALS(50, 33, -1)
            VILLAGER_ARMBAND(50, 50, -1)
            OPAL_MACHETE(50, 150, -1)
            JADE_MACHETE(50, 333, -1)
            RED_TOPAZ_MACHETE(50, 600, -1)
            TRIBAL_TOP_6351(50, 100, -1)
            VILLAGER_ROBE_6353(50, 83, 250)
            VILLAGER_HAT_6355(50, 66, -1)
            VILLAGER_SANDALS_6357(50, 33, -1)
            VILLAGER_ARMBAND_6359(50, 50, -1)
            UNCUT_OPAL(0, 12, -1)
            UNCUT_JADE(0, 18, -1)
            UNCUT_RED_TOPAZ(0, 24, -1)
            TRIBAL_TOP_6361(50, 100, -1)
            VILLAGER_ROBE_6363(50, 83, 250)
            VILLAGER_HAT_6365(50, 66, -1)
            VILLAGER_SANDALS_6367(50, 33, -1)
            VILLAGER_ARMBAND_6369(50, 50, -1)
            OPAL(0, 60, -1)
            JADE(0, 90, -1)
            RED_TOPAZ(0, 120, -1)
            TRIBAL_TOP_6371(50, 100, -1)
            VILLAGER_ROBE_6373(50, 83, 250)
            VILLAGER_HAT_6375(50, 66, -1)
            VILLAGER_SANDALS_6377(50, 33, -1)
            VILLAGER_ARMBAND_6379(50, 50, -1)
            GOUT_TUBER(0, 60, -1)
        }
    }
}
