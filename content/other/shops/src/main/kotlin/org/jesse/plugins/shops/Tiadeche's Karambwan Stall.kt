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

class TiadecheSKarambwanStall : ShopScript() {

    init {
        "Tiadeche's Karambwan Stall"(112, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_KARAMBWAN(10, 90, 110)
            RAW_KARAMBWANJI(50, 0, 10)
            KARAMBWAN_VESSEL(2, 2, 2)
        }
    }
}
