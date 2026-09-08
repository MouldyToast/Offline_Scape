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

class WayneSChainsChainmailSpecialist : ShopScript() {

    init {
        "Wayne's Chains - Chainmail Specialist"(14, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_CHAINBODY(3, 39, 60)
            IRON_CHAINBODY(2, 136, 210)
            STEEL_CHAINBODY(1, 487, 750)
            BLACK_CHAINBODY(1, 936, 1440)
            MITHRIL_CHAINBODY(1, 1267, 1950)
            ADAMANT_CHAINBODY(1, 3120, 4800)
        }
    }
}
