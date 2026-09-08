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

class QuartermasterSStores : ShopScript() {

    init {
        "Quartermaster's Stores"(183, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, 0, 1)
            JUG(2, 0, 1)
            SHEARS(3, 0, 1)
            TINDERBOX(3, 0, 1)
            BREAD(10, 5, 15)
            BRONZE_HALBERD(10, 32, 104)
            IRON_HALBERD(10, 112, 364)
            STEEL_HALBERD(10, 400, 1300)
            BLACK_HALBERD(10, 768, 2496)
            MITHRIL_HALBERD(7, 1040, 3380)
            ADAMANT_HALBERD(7, 2560, 8320)
            RUNE_HALBERD(7, 25600, 83200)
            DRAGON_HALBERD(5, 100000, 325000)
        }
    }
}
