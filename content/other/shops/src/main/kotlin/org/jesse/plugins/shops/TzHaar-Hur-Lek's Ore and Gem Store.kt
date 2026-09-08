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

class TzhaarHurLekSOreAndGemStore : ShopScript() {

    init {
        "TzHaar-Hur-Lek's Ore and Gem Store"(113, ShopCurrency.TOKKUL, STOCK_ONLY) {
            TIN_ORE(5, 0, 4)
            COPPER_ORE(5, 0, 4)
            IRON_ORE(2, 2, 25)
            SILVER_ORE(0, 11, 112)
            COAL(0, 6, 67)
            GOLD_ORE(0, 22, 225)
            MITHRIL_ORE(0, 24, 243)
            ADAMANTITE_ORE(0, 60, 600)
            RUNITE_ORE(0, 480, 4800)
            UNCUT_SAPPHIRE(1, 3, 37)
            UNCUT_EMERALD(1, 7, 75)
            UNCUT_RUBY(0, 15, 150)
            UNCUT_DIAMOND(0, 30, 300)
            UNCUT_DRAGONSTONE(0, 150, 1500)
            UNCUT_ONYX(1, 30000, 300000)
            ONYX_BOLT_TIPS(50, 150, 1500)
        }
    }
}
