package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class SkulgrimenSBattleGear : ShopScript() {

    init {
        "Skulgrimen's Battle Gear"(53, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_WARHAMMER(5, -1, 61)
            IRON_WARHAMMER(4, -1, 224)
            STEEL_WARHAMMER(3, -1, 832)
            BLACK_WARHAMMER(3, -1, 1274)
            MITHRIL_WARHAMMER(2, -1, 2158)
            ADAMANT_WARHAMMER(1, -1, 5356)
            RUNE_WARHAMMER(0, -1, 53950)
            ARCHER_HELM(5, 42000, 78000)
            BERSERKER_HELM(5, 42000, 78000)
            WARRIOR_HELM(5, 42000, 78000)
            FARSEER_HELM(5, 42000, 78000)
        }
    }
}
