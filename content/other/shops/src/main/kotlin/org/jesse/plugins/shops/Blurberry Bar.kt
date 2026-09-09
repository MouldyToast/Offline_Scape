package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class BlurberryBar : ShopScript() {

    init {
        "Blurberry Bar"(93, ShopCurrency.COINS, STOCK_ONLY) {
            PREMADE_BLURB_SP(10, 18, 30)
            PREMADE_CHOC_SDY(10, 18, 30)
            PREMADE_DR_DRAGON(10, 18, 30)
            PREMADE_FR_BLAST(10, 18, 30)
            PREMADE_P_PUNCH(10, 18, 30)
            PREMADE_SGG(10, 18, 30)
            PREMADE_WIZ_BLZD(10, 18, 30)
        }
    }
}
