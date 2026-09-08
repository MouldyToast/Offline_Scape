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

class GianneSRestaurant : ShopScript() {

    init {
        "Gianne's Restaurant"(89, ShopCurrency.COINS, STOCK_ONLY) {
            PREMADE_VEG_BATTA(3, 30, 120)
            PREMADE_WM_BATTA(3, 30, 120)
            PREMADE_TD_BATTA(3, 30, 120)
            PREMADE_FRT_BATTA(3, 30, 120)
            PREMADE_CT_BATTA(3, 30, 120)
            PREMADE_WORM_HOLE(3, 37, 150)
            PREMADE_TTL(3, 40, 160)
            PREMADE_VEG_BALL(3, 37, 150)
            PREMADE_CHOC_BOMB(3, 40, 160)
            PREMADE_WM_CRUN(3, 21, 85)
            PREMADE_TD_CRUNCH(3, 21, 85)
            PREMADE_CH_CRUNCH(3, 21, 85)
            PREMADE_SY_CRUNCH(3, 21, 85)
        }
    }
}
