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

class NardahHunterShop : ShopScript() {

    init {
        "Nardah Hunter Shop"(134, ShopCurrency.COINS, STOCK_ONLY) {
            BUTTERFLY_NET(5, 14, 24)
            BUTTERFLY_JAR(100, 0, 1)
            MAGIC_BOX(30, 420, 720)
            NOOSE_WAND(50, 2, 4)
            BIRD_SNARE(50, 3, 6)
            BOX_TRAP(25, 22, 38)
            TEASING_STICK(5, 35, 60)
            UNLIT_TORCH(20, 2, 4)
            RABBIT_SNARE(10, 10, 18)
            BIRD_SNARE_PACK(3, 353, 606)
            BOX_TRAP_PACK(3, 2240, 3840)
            MAGIC_IMP_BOX_PACK(3, 42000, 72000)
        }
    }
}
