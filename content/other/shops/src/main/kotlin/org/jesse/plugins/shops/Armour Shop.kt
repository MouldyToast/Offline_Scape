package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ArmourShop : ShopScript() {

    init {
        "Armour Shop"(35, ShopCurrency.COINS, STOCK_ONLY) {
            MITHRIL_CHAINBODY(4, 1365, 2145)
            MITHRIL_MED_HELM(4, 546, 858)
            MITHRIL_FULL_HELM(4, 1001, 1573)
            MITHRIL_SQ_SHIELD(4, 1092, 1716)
            MITHRIL_KITESHIELD(4, 1547, 2431)
            MITHRIL_PLATELEGS(4, 1820, 2860)
            MITHRIL_PLATESKIRT(4, 1820, 2860)
            MITHRIL_PLATEBODY(4, 3640, 5720)
        }
    }
}
