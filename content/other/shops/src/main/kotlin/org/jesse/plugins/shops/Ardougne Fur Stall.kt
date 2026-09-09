package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ArdougneFurStall : ShopScript() {

    init {
        "Ardougne Fur Stall"(79, ShopCurrency.COINS, STOCK_ONLY) {
            BEAR_FUR(3, -1, 12)
            GREY_WOLF_FUR(3, -1, 60)
            POLAR_KEBBIT_FUR(0, 13, 12)
            COMMON_KEBBIT_FUR(0, 14, 14)
            FELDIP_WEASEL_FUR(0, 16, 16)
            DESERT_DEVIL_FUR(0, 20, 20)
            TATTY_LARUPIA_FUR(0, 61, 72)
            LARUPIA_FUR(0, 81, 96)
            TATTY_GRAAHK_FUR(0, 91, 108)
            GRAAHK_FUR(0, 122, 144)
            TATTY_KYATT_FUR(0, 144, 144)
            KYATT_FUR(0, 192, 192)
        }
    }
}
