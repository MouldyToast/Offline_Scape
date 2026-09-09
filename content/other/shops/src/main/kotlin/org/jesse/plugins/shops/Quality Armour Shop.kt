package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class QualityArmourShop : ShopScript() {

    init {
        "Quality Armour Shop"(196, ShopCurrency.COINS, STOCK_ONLY) {
            STEEL_CHAINBODY(3, 225, 750)
            MITHRIL_CHAINBODY(1, 585, 1950)
            BLACK_CHAINBODY(1, 432, 1440)
            ADAMANT_CHAINBODY(1, 1440, 4800)
            STEEL_MED_HELM(3, 90, 300)
            MITHRIL_MED_HELM(1, 234, 1014)
            ADAMANT_MED_HELM(1, 576, 2496)
            STEEL_SQ_SHIELD(0, 180, 600)
            BLACK_KITESHIELD(0, 636, 2121)
        }
    }
}
