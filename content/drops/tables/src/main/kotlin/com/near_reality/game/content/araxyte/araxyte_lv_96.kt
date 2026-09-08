package com.near_reality.game.content.araxyte

import com.near_reality.game.content.slayer.RegularTask
import com.near_reality.scripts.npc.drops.table.chance.dynamic.DynamicRollItemOneIn
import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ids.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class AraxyteLv96Droptable : NPCDropTableScript() {

    init {
        npcs(ARAXYTE_LV_96)

        onDeath {
            npc.dropItem(killer, Item(ARAXYTE_VENOM_SACK))
            rollStaticTableAndDrop(killer, Tertiary)
            rollStaticTableAndDrop(killer, Main)
        }

        buildTable(100) {
            Main {
                // Weapons and Armor
                ADAMANT_LONGSWORD quantity 1 oneIn 25
                ADAMANT_BATTLEAXE quantity 1 oneIn 25
                RUNE_DAGGER quantity 1 oneIn 42
                RUNE_MED_HELM quantity 1 oneIn 63
                RUNE_PLATELEGS quantity 1 oneIn 63
                ARANEA_BOOTS quantity 1 dynamicOneIn {
                    if(this.slayer.assignment.task == RegularTask.ARAXYTES)
                        600
                    else
                        4000
                }
                // Runes and Ammo
                AIR_RUNE quantity IntRange(120, 140) oneIn 13
                WATER_RUNE quantity IntRange(120, 140) oneIn 13
                EARTH_RUNE quantity IntRange(120, 140) oneIn 13
                FIRE_RUNE quantity IntRange(120, 140) oneIn 13
                COSMIC_RUNE quantity IntRange(7, 12) oneIn 25
                CHAOS_RUNE quantity IntRange(10, 15) oneIn 25
                NATURE_RUNE quantity IntRange(15, 20) oneIn 25
                DEATH_RUNE quantity IntRange(20, 25) oneIn 25
                BLOOD_RUNE quantity IntRange(15, 18) oneIn 25
                SOUL_RUNE quantity IntRange(9, 12) oneIn 25
                // Herbs
                GRIMY_KWUARM quantity 1 oneIn 40
                GRIMY_DWARF_WEED quantity 1 oneIn 51
                GRIMY_CADANTINE quantity 1 oneIn 51
                GRIMY_LANTADYME quantity 1 oneIn 68
                GRIMY_AVANTOE quantity 1 oneIn 82
                GRIMY_RANARR_WEED quantity 1 oneIn 102
                GRIMY_SNAPDRAGON quantity 1 oneIn 102
                GRIMY_TORSTOL quantity 1 oneIn 136
                // Seeds
                RANARR_SEED quantity 1 oneIn 425
                SNAPDRAGON_SEED quantity 1 oneIn 455
                TORSTOL_SEED quantity 1 oneIn 580
                WATERMELON_SEED quantity 15 oneIn 607
                WILLOW_SEED quantity 1 oneIn 637
                MAHOGANY_SEED quantity 1 oneIn 708
                MAPLE_SEED quantity 1 oneIn 708
                TEAK_SEED quantity 1 oneIn 708
                YEW_SEED quantity 1 oneIn 708
                PAPAYA_TREE_SEED quantity 1 oneIn 911
                MAGIC_SEED quantity 1 oneIn 1159
                PALM_TREE_SEED quantity 1 oneIn 1275
                SPIRIT_SEED quantity 1 oneIn 1594
                DRAGONFRUIT_TREE_SEED quantity 1 oneIn 2125
                CELASTRUS_SEED quantity 1 oneIn 3188
                REDWOOD_TREE_SEED quantity 1 oneIn 3188
                // Other
                COINS_995 quantity IntRange(800, 1200) oneIn 13
                ARAXYTE_VENOM_SACK quantity 2 oneIn 25
                ARAXYTE_HEAD quantity 1 oneIn 2000
            }

            Tertiary {
                SCROLL_BOX_ELITE quantity 1 oneIn 128
            }
        }

        provideInfo<DynamicRollItemOneIn> {
            if(this.id == ARANEA_BOOTS)
                "This rate decreases to 1/600 when on an araxyte slayer task"
            else
                null
        }
    }
}
