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

class MageArenaStaffs : ShopScript() {

    init {
        "Mage Arena Staffs"(245, ShopCurrency.COINS, STOCK_ONLY) {
            SARADOMIN_STAFF(5, 0, 80000)
            GUTHIX_STAFF(5, 0, 80000)
            ZAMORAK_STAFF(5, 0, 80000)
        }
    }
}
