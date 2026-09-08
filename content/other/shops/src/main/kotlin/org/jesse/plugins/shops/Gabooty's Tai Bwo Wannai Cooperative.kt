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
