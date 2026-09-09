package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class JenniferSGeneralFieldSupplies : ShopScript() {

    init {
        "Jennifer's General Field Supplies"(221, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, -1, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, -1, 1)
            BUCKET(3, -1, 2)
            BOWL(2, -1, 5)
            CAKE_TIN(2, -1, 13)
            TINDERBOX(2, -1, 1)
            CHISEL(2, -1, 1)
            BRONZE_AXE(10, -1, 390)
            FIELD_RATION(25, -1, 390)
        }
    }
}
