package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class SedduSAdventurersStore : ShopScript() {

    init {
        "Seddu's Adventurers' Store"(136, ShopCurrency.COINS, STOCK_ONLY) {
            RUNE_PLATESKIRT(1, -1, 60800)
            RUNE_PLATELEGS(1, 41600, 60800)
            RUNE_CHAINBODY(1, 33500, 47500)
            GREEN_DHIDE_CHAPS(1, 2613, 3705)
            GREEN_DHIDE_VAMBRACES(1, -1, 2375)
            STEEL_KITESHIELD(1, -1, 807)
            BLACK_MED_HELM(1, -1, 547)
        }
    }
}
