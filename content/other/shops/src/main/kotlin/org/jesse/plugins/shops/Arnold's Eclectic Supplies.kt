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

class ArnoldSEclecticSupplies : ShopScript() {

    init {
        "Arnold's Eclectic Supplies"(86, ShopCurrency.COINS, STOCK_ONLY) {
            SMALL_FISHING_NET(2, 2, 5)
            HARPOON(2, 2, 5)
            RAW_MONKFISH(1, 126, 241)
            MONKFISH(0, 126, 241)
            BREAD(1, 6, 12)
            POT(4, 0, 1)
            BUCKET_OF_MILK(1, 4, 6)
            NEEDLE(3, 0, 1)
            THREAD(15, 0, 1)
            BEER(10, 1, 2)
            GLASSBLOWING_PIPE(2, 1, 2)
            KNIFE(1, 3, 6)
        }
    }
}
