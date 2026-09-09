package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class WarriorGuildPotionShop : ShopScript() {

    init {
        "Warrior Guild Potion Shop"(69, ShopCurrency.COINS, STOCK_ONLY) {
            STRENGTH_POTION3(10, 1, 15)
            ATTACK_POTION3(10, 1, 14)
            DEFENCE_POTION3(10, 18, 144)
        }
    }
}
