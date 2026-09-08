package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

class FunchSFineGroceries : ShopScript() {

    init {
        "Funch's Fine Groceries"(88, ShopCurrency.COINS, STOCK_ONLY) {
            BRANDY(10, 1, 5)
            GIN(10, 1, 5)
            VODKA(10, 1, 5)
            WHISKY(10, 1, 5)
            PINEAPPLE(10, 1, 2)
            EQUA_LEAVES(20, -1, 2)
            ORANGE(20, -1, 2)
            LEMON(20, -1, 2)
            LIME(20, -1, 2)
            DWELLBERRIES(5, -1, 4)
            COCKTAIL_SHAKER(10, 1, 2)
            CHOCOLATE_BAR(20, 6, 10)
            CHOCOLATE_DUST(10, 6, 2)
            POT_OF_CREAM(5, 6, 2)
            BUCKET_OF_MILK(5, 4, 6)
            KNIFE(5, 8, 6)
            COCKTAIL_GLASS(20, 1, 1)
        }
    }
}
