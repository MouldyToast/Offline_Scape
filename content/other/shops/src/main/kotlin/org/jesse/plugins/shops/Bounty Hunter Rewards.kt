package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class BountyHunterRewards : ShopScript() {
    init {
        "Bounty Hunter Rewards"(ShopCurrency.BH_POINTS, STOCK_ONLY) {
            ESOTERIC_EMBLEM_TIER_1(1000, -1, 2)
            TARGET_TELEPORT(1000, -1, 5)
            BOUNTY_TELEPORT_SCROLL(1000, -1, 120)
            VESTAS_LONGSWORD_BH(100, 400, 650)
            STATIUSS_WARHAMMER_BH(100, 300, 450)
            MORRIGANS_THROWING_AXE_BH(100, 175, 300)
            MORRIGANS_JAVELIN_BH(100, 325, 550)
            ZURIELS_STAFF_BH(100, 200, 300)
            VESTAS_CHAINBODY_27831(100, 350, 600)
            VESTAS_PLATESKIRT_27832(100, 325, 550)
            STATIUSS_FULL_HELM_27833(100, 275, 400)
            STATIUSS_PLATEBODY_27834(100, 400, 600)
            STATIUSS_PLATELEGS_27835(100, 375, 500)
            MORRIGANS_COIF_27836(100, 270, 400)
            MORRIGANS_LEATHER_BODY_27837(100, 400, 600)
            MORRIGANS_LEATHER_CHAPS_27838(100, 375, 500)
            ZURIELS_HOOD_27839(100, 200, 300)
            ZURIELS_ROBE_TOP_27840(100, 325, 500)
            ZURIELS_ROBE_BOTTOM_27841(100, 275, 400)
            DARK_BOW_IMBUE_SCROLL(100, 75, 100)
            BARRELCHEST_ANCHOR_IMBUE_SCROLL(100, 50, 75)
            DRAGON_MACE_IMBUE_SCROLL(100, 25, 50)
            DRAGON_LONGSWORD_IMBUE_SCROLL(100, 25, 50)
            ABYSSAL_DAGGER_IMBUE_SCROLL(100, 100, 150)
            BOUNTY_HUNTER_ORNAMENT_KIT(100, 25, 50)
            ELDER_MAUL_ORNAMENT_KIT(100, 25, 50)
            HEAVY_BALLISTA_ORNAMENT_KIT(100, 25, 50)
            ELDER_CHAOS_ROBES_ORNAMENT_KIT(100, 25, 50)
        }
    }
}
