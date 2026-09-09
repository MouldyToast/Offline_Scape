package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ZaffSSuperiorStaffs : ShopScript() {

    init {
        "Zaff's Superior Staffs!"(167, ShopCurrency.COINS, STOCK_ONLY) {
            BATTLESTAFF(5, 3850, 7000)
            STAFF(5, 8, 15)
            MAGIC_STAFF(5, 110, 200)
            STAFF_OF_AIR(2, 825, 1500)
            STAFF_OF_WATER(2, 825, 1500)
            STAFF_OF_EARTH(2, 825, 1500)
            STAFF_OF_FIRE(2, 825, 1500)
        }
    }
}
