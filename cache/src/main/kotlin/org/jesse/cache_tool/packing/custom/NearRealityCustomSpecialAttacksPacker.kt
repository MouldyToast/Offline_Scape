package org.jesse.cache_tool.packing.custom

import org.jesse.game.item.ids.*
import mgi.types.config.enums.EnumDefinitions

object NearRealityCustomSpecialAttacksPacker {

    @JvmStatic
    fun pack(){
        // special attack descriptions
        EnumDefinitions.get(1739).apply {
            values[HOLY_GREAT_WARHAMMER] = "Smash: Deal an attack that inflicts 50% more damage and lowers your target's Defence level by 30%."
            values[32161] = "Enhanced Sanctuary: In addition to a better boost in defence (15% + 2 boost), heals the player 10 hp every 2 seconds for 10 seconds for a total of 50 hp."
            values[BARRELCHEST_ANCHOR_BH] = "Drains the target's combat levels equivalent to 10% of the damage dealt. Drains in the following order: Defence, Attack, Ranged, then Magic.";
            values[DRAGON_MACE_BH] = "Increases damage and accuracy for one hit."
            values[DRAGON_LONGSWORD_BH] = "Deals 25% more damage."
            values[DARK_BOW_BH] = "Launches a double attack."
            values[ABYSSAL_DAGGER_BH] = "Hits the target twice, either hitting or missing both attacks."
            values[ABYSSAL_DAGGER_BHP] = values[ABYSSAL_DAGGER_BH]
            values[ABYSSAL_DAGGER_BHP_27865] = values[ABYSSAL_DAGGER_BH]
            values[ABYSSAL_DAGGER_BHP_27867] = values[ABYSSAL_DAGGER_BH]

            values[BURNING_CLAWS] = "Attacks 3 times in quick succession, with a chance to burn the enemy on each hit."
            values[PURGING_STAFF] = "Requires being on the Arceuus spellbook." +
                    "Uses the best demonbane spell the player can cast; if the demonic creature targeted dies from Scatter ashes, the special attack energy used is refunded, and the player's next attack can be used three ticks earlier."
            values[SCORCHING_BOW] = "Binds demonic creatures for 12 seconds (20 ticks), dealing 1 burn damage in the process." +
                    "It deals an additional 1 burn damage every 4 ticks (2.4 seconds), for a total of 5 burn damage. Cannot be used against non-demonic monsters."
            values[EMBERLIGHT] = "Drains the target's Attack, Strength, and Defence by 5% of their level + 1." +
                    "Three times as effective on demons (reduces each stat by 15%)." +
                    "Special is only activated on successful hits."
            values[ELDER_MAUL] = "Lowers the target's current Defence level by 35% on a successful hit. The effect is stackable and relative to the target's"
            values[ELDER_MAUL_OR] = "Lowers the target's current Defence level by 35% on a successful hit. The effect is stackable and relative to the target's"
            values[CORRUPTED_VOIDWAKER] = values[VOIDWAKER_27690]
            values[CORRUPTED_DRAGON_CLAWS] = values[DRAGON_CLAWS]
            values[CORRUPTED_ARMADYL_GODSWORD] = values[ARMADYL_GODSWORD]
            values[CORRUPTED_DARK_BOW] = values[DARK_BOW]
            values[CORRUPTED_VOLATILE_NIGHTMARE_STAFF] = values[VOLATILE_NIGHTMARE_STAFF]
            pack()
        }
        // special attack energy cost
        EnumDefinitions.get(906).apply {
            values[CORRUPTED_VOIDWAKER] = values[VOIDWAKER_27690]
            values[CORRUPTED_DRAGON_CLAWS] = values[DRAGON_CLAWS]
            values[CORRUPTED_ARMADYL_GODSWORD] = values[ARMADYL_GODSWORD]
            values[CORRUPTED_DARK_BOW] = values[DARK_BOW]
            values[CORRUPTED_VOLATILE_NIGHTMARE_STAFF] = values[VOLATILE_NIGHTMARE_STAFF]
            values[HOLY_GREAT_WARHAMMER] = 500
            values[32161] = 500
            values[11235] = 500
            values[12765] = 500
            values[12766] = 500
            values[12767] = 500
            values[12768] = 500
            values[ABYSSAL_DAGGER] = 250
            values[ABYSSAL_DAGGER_P] = 250
            values[ABYSSAL_DAGGER_P_13269] = 250
            values[ABYSSAL_DAGGER_P_13271] = 250

            values[BARRELCHEST_ANCHOR_BH] = 500
            values[DRAGON_MACE_BH] = 250
            values[DRAGON_LONGSWORD_BH] = 250
            values[DARK_BOW_BH] = 450
            values[ABYSSAL_DAGGER_BH] = 350
            values[ABYSSAL_DAGGER_BHP] = 350
            values[ABYSSAL_DAGGER_BHP_27865] = 350
            values[ABYSSAL_DAGGER_BHP_27867] = 350
            values[STATIUSS_WARHAMMER_BH] = 350
            values[VESTAS_LONGSWORD_BH] = 250
            values[VESTAS_SPEAR_BH] = 500
            values[MORRIGANS_JAVELIN_BH] = 500
            values[MORRIGANS_THROWING_AXE_BH] = 500

            values[DRAGON_DAGGER_CR] = 250
            values[DRAGON_DAGGER_PCR] = 250
            values[DRAGON_DAGGER_PCR_28023] = 250
            values[DRAGON_DAGGER_PCR_28025] = 250
            values[DRAGON_MACE_CR] = 150
            values[DRAGON_SWORD_CR] = 400
            values[DRAGON_SCIMITAR_CR] = 550
            values[DRAGON_LONGSWORD_CR] = 250
            values[DRAGON_WARHAMMER_CR] = 500
            values[DRAGON_BATTLEAXE_CR] = 1000
            values[DRAGON_CLAWS_CR] = 500
            values[DRAGON_SPEAR_CR] = 250
            values[DRAGON_SPEAR_PCR] = 250
            values[DRAGON_SPEAR_PCR_28045] = 250
            values[DRAGON_SPEAR_PCR_28047] = 250
            values[DRAGON_HALBERD_CR] = 300
            values[DRAGON_2H_SWORD_CR] = 600
            values[DRAGON_CROSSBOW_CR] = 600

            values[BURNING_CLAWS] = 300
            values[PURGING_STAFF] = 250
            values[SCORCHING_BOW] = 250
            values[EMBERLIGHT] = 500

            values[ELDER_MAUL] = 500
            values[ELDER_MAUL_OR] = 500

            pack()
        }
    }
}