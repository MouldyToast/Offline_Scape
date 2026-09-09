package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class WarriorGuildFoodShop : ShopScript() {

    init {
        "Warrior Guild Food Shop"(68, ShopCurrency.COINS, STOCK_ONLY) {
            TROUT(10, 3, 24)
            BASS(10, 18, 144)
            PLAIN_PIZZA(5, 6, 47)
            POTATO_WITH_CHEESE(10, 1, 9)
            STEW(10, 3, 24)
        }
    }
}
