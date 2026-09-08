package com.near_reality.game.content.combat

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_BH
import com.zenyte.game.item.ItemId.BARRELCHEST_ANCHOR_BH
import com.zenyte.game.item.ItemId.DARK_BOW_BH
import com.zenyte.game.item.ItemId.DRAGON_2H_SWORD_CR
import com.zenyte.game.item.ItemId.DRAGON_BATTLEAXE_CR
import com.zenyte.game.item.ItemId.DRAGON_CLAWS_CR
import com.zenyte.game.item.ItemId.DRAGON_CROSSBOW_CR
import com.zenyte.game.item.ItemId.DRAGON_DAGGER_CR
import com.zenyte.game.item.ItemId.DRAGON_HALBERD_CR
import com.zenyte.game.item.ItemId.DRAGON_LONGSWORD_BH
import com.zenyte.game.item.ItemId.DRAGON_LONGSWORD_CR
import com.zenyte.game.item.ItemId.DRAGON_MACE_BH
import com.zenyte.game.item.ItemId.DRAGON_MACE_CR
import com.zenyte.game.item.ItemId.DRAGON_SCIMITAR_CR
import com.zenyte.game.item.ItemId.DRAGON_SPEAR_CR
import com.zenyte.game.item.ItemId.DRAGON_SWORD_CR
import com.zenyte.game.item.ItemId.DRAGON_WARHAMMER_CR
import com.zenyte.game.item.ItemId.ELDER_MAUL_OR
import com.zenyte.game.item.ItemId.HEAVY_BALLISTA_OR
import com.zenyte.game.item.ItemId.MORRIGANS_JAVELIN_BH
import com.zenyte.game.item.ItemId.MORRIGANS_THROWING_AXE_BH
import com.zenyte.game.item.ItemId.STATIUSS_WARHAMMER_BH
import com.zenyte.game.item.ItemId.VESTAS_LONGSWORD_BH
import com.zenyte.game.item.ItemId.VESTAS_SPEAR_BH
import com.zenyte.game.item.ItemId.ZURIELS_STAFF_BH


object CombatUtility {
    @JvmStatic fun hasBountyHunterWeapon(player: Player) : Boolean {
        when(player.weapon?.id) {
            VESTAS_LONGSWORD_BH,
            VESTAS_SPEAR_BH,
            STATIUSS_WARHAMMER_BH,
            MORRIGANS_THROWING_AXE_BH,
            MORRIGANS_JAVELIN_BH,
            ZURIELS_STAFF_BH -> return true
        }
        return false
    }

    @JvmStatic fun hasBHImbuedWeapon(player: Player) : Boolean {
        when(player.weapon?.id) {
            DARK_BOW_BH,
            BARRELCHEST_ANCHOR_BH,
            DRAGON_MACE_BH,
            DRAGON_LONGSWORD_BH,
            ABYSSAL_DAGGER_BH -> return true
        }
        return false
    }

    @JvmStatic fun hasBHKittedWeapon(player: Player) : Boolean {
        when(player.weapon?.id) {
            ELDER_MAUL_OR,
            HEAVY_BALLISTA_OR,
            DRAGON_2H_SWORD_CR,
            DRAGON_BATTLEAXE_CR,
            DRAGON_CLAWS_CR,
            DRAGON_CROSSBOW_CR,
            DRAGON_DAGGER_CR,
            DRAGON_HALBERD_CR,
            DRAGON_LONGSWORD_CR,
            DRAGON_MACE_CR,
            DRAGON_SCIMITAR_CR,
            DRAGON_SPEAR_CR,
            DRAGON_SWORD_CR,
            DRAGON_WARHAMMER_CR, -> return true
        }
        return false
    }
}
