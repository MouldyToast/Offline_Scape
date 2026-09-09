package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class MulticannonParts : ShopScript() {

    init {
        "Multicannon Parts"(6, ShopCurrency.COINS, STOCK_ONLY) {
            AMMO_MOULD(5, 0, 5)
            INSTRUCTION_MANUAL(5, 0, 10)
        //    CANNON_BASE(5, 0, 375000)
        //    CANNON_STAND(5, 0, 375000)
        //    CANNON_BARRELS(5, 0, 375000)
        //    CANNON_FURNACE(5, 0, 375000)
        }
    }
}
