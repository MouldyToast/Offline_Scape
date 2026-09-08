package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ids.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class CastleWarsStore : ShopScript() {

    init {
        "Castle Wars Store"(300, ShopCurrency.CASTLE_WARS_TICKETS, STOCK_ONLY) {
            DECORATIVE_HELM(100, 0, 4)
            DECORATIVE_ARMOUR(100, 0, 8)
            DECORATIVE_SWORD(100, 0, 5)
            DECORATIVE_SHIELD(100, 0, 6)
            DECORATIVE_ARMOUR_4070(100, 0, 6)
            DECORATIVE_ARMOUR_11893(100, 0, 6)
            DECORATIVE_HELM_4506(100, 0, 40)
            DECORATIVE_ARMOUR_4504(100, 0, 80)
            DECORATIVE_SWORD_4503(100, 0, 50)
            DECORATIVE_SHIELD_4507(100, 0, 60)
            DECORATIVE_ARMOUR_4505(100, 0, 60)
            DECORATIVE_ARMOUR_11894(100, 0, 60)
            DECORATIVE_HELM_4511(100, 0, 400)
            DECORATIVE_ARMOUR_4509(100, 0, 800)
            DECORATIVE_SWORD_4508(100, 0, 500)
            DECORATIVE_SHIELD_4512(100, 0, 600)
            DECORATIVE_ARMOUR_4510(100, 0, 600)
            DECORATIVE_ARMOUR_11895(100, 0, 600)
            CASTLEWARS_HOOD(100, 0, 10)
            CASTLEWARS_CLOAK(100, 0, 10)
            CASTLEWARS_HOOD_4515(100, 0, 10)
            CASTLEWARS_CLOAK_4516(100, 0, 10)
            DRAGON_LONGSWORD(100, 0, 100)
            DRAGON_LONGSWORD(100, 0, 100)
            DECORATIVE_ARMOUR_11898(100, 0, 20)
            DECORATIVE_ARMOUR_11896(100, 0, 40)
            DECORATIVE_ARMOUR_11897(100, 0, 30)
            DECORATIVE_ARMOUR_11899(100, 0, 40)
            DECORATIVE_ARMOUR_11900(100, 0, 30)
            DECORATIVE_ARMOUR_11901(100, 0, 40)
            SARADOMIN_HALO(100, 0, 75)
            ZAMORAK_HALO(100, 0, 75)
            GUTHIX_HALO(100, 0, 75)
        }
    }
}
