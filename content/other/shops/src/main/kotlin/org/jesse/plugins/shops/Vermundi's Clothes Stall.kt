package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class VermundiSClothesStall : ShopScript() {

    init {
        "Vermundi's Clothes Stall"(192, ShopCurrency.COINS, STOCK_ONLY) {
            SKIRT(3, 192, 455)
            TROUSERS(3, 302, 715)
            SHORTS(3, 154, 364)
            WOVEN_TOP(3, 275, 650)
            SHIRT(3, 247, 585)
            SILK(5, 16, 39)
        }
    }
}
