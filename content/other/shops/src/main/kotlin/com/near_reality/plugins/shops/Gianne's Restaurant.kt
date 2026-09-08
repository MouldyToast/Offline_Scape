package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.zenyte.game.item.ItemId.PREMADE_CHOC_BOMB
import com.zenyte.game.item.ItemId.PREMADE_CH_CRUNCH
import com.zenyte.game.item.ItemId.PREMADE_CT_BATTA
import com.zenyte.game.item.ItemId.PREMADE_FRT_BATTA
import com.zenyte.game.item.ItemId.PREMADE_SY_CRUNCH
import com.zenyte.game.item.ItemId.PREMADE_TD_BATTA
import com.zenyte.game.item.ItemId.PREMADE_TD_CRUNCH
import com.zenyte.game.item.ItemId.PREMADE_TTL
import com.zenyte.game.item.ItemId.PREMADE_VEG_BALL
import com.zenyte.game.item.ItemId.PREMADE_VEG_BATTA
import com.zenyte.game.item.ItemId.PREMADE_WM_BATTA
import com.zenyte.game.item.ItemId.PREMADE_WM_CRUN
import com.zenyte.game.item.ItemId.PREMADE_WORM_HOLE

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
