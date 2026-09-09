package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class BarkerSHaberdashery : ShopScript() {

    init {
        "Barker's Haberdashery"(171, ShopCurrency.COINS, STOCK_ONLY) {
            GREY_BOOTS(5, 200, 650)
            GREY_ROBE_TOP(5, 200, 650)
            GREY_ROBE_BOTTOMS(5, 200, 650)
            GREY_HAT(5, 200, 650)
            GREY_GLOVES(5, 200, 650)
            RED_BOOTS(5, 200, 650)
            RED_ROBE_TOP(5, 200, 650)
            RED_ROBE_BOTTOMS(5, 200, 650)
            RED_HAT(5, 200, 650)
            RED_GLOVES(5, 200, 650)
            YELLOW_BOOTS(5, 200, 650)
            YELLOW_ROBE_TOP(5, 200, 650)
            YELLOW_ROBE_BOTTOMS(5, 200, 650)
            YELLOW_HAT(5, 200, 650)
            YELLOW_GLOVES(5, 200, 650)
            TEAL_BOOTS(5, 200, 650)
            TEAL_ROBE_TOP(5, 200, 650)
            TEAL_ROBE_BOTTOMS(5, 200, 650)
            TEAL_HAT(5, 200, 650)
            TEAL_GLOVES(5, 200, 650)
            PURPLE_BOOTS(5, 200, 650)
            PURPLE_ROBE_TOP(5, 200, 650)
            PURPLE_ROBE_BOTTOMS(5, 200, 650)
            PURPLE_HAT(5, 200, 650)
            PURPLE_GLOVES(5, 200, 650)
            RED_CAPE(5, 0, 2)
            BLACK_CAPE(5, 2, 9)
            BLUE_CAPE(5, 12, 41)
            YELLOW_CAPE(5, 12, 41)
            GREEN_CAPE(5, 12, 41)
        }
    }
}
