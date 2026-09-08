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

class NardokSBoneWeapons : ShopScript() {

    init {
        "Nardok's Bone Weapons"(145, ShopCurrency.COINS, STOCK_ONLY) {
            BONE_CLUB(10, 360, 600)
            BONE_SPEAR(10, 360, 600)
            BONE_DAGGER(5, 1200, 2000)
            DORGESHUUN_CROSSBOW(5, 1200, 2000)
            BONE_BOLTS(1000, 1, 3)
            BONE_BOLT_PACK(80, 210, 350)
        }
    }
}
