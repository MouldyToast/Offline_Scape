package org.jesse.game.content.tormented_demon

import org.jesse.scripts.npc.drops.table.noted
import org.jesse.game.item.Item
import org.jesse.game.util.Utils
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class TormentedDemonDroptable : NPCDropTableScript() {

    init {
        npcs(TORRMENTED_DEMON, TORRMENTED_DEMON_13600, TORRMENTED_DEMON_13601)

        onDeath {
            rollStaticTableAndDrop(killer, Tertiary)
            if (Utils.random(500) == 0)
                rollStaticTableAndDrop(killer, Unique)
            else
                rollStaticTableAndDrop(killer, Main)
            npc.dropItem(killer, Item(INFERNAL_ASHES))
        }

        buildTable(100) {
            Unique {
                TORMENTED_SYNAPSE quantity 1 oneIn 500
                BURNING_CLAW quantity 1 oneIn 501
            }

            Main {
                // Weapons and Armor
                RUNE_PLATEBODY quantity 1 oneIn 13
                DRAGON_DAGGER quantity 1 oneIn 17
                BATTLESTAFF quantity 1.noted oneIn 17
                RUNE_KITESHIELD quantity 1 oneIn 26
                // Runes and Ammo
                CHAOS_RUNE quantity IntRange(25, 100) oneIn 13
                RUNE_ARROW quantity IntRange(65, 125) oneIn 13
                SOUL_RUNE quantity IntRange(50, 75) oneIn 26
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
                // Consumables
                MANTA_RAY quantity IntRange(1, 2) oneIn 13
                SMOULDERING_GLAND quantity 1 oneIn 25
                SMOULDERING_PILE_OF_FLESH quantity 1 oneIn 25
                PRAYER_POTION4 quantity 1 oneIn 51
                PRAYER_POTION2 quantity 1 oneIn 51
                SMOULDERING_HEART quantity 1 oneIn 125
                // Other
                MAGIC_SHORTBOW_U quantity 1.noted oneIn 9
                GUTHIXIAN_TEMPLE_TELEPORT quantity 2 oneIn 12
                MALICIOUS_ASHES quantity IntRange(2, 3) oneIn 26
                FIRE_ORB quantity IntRange(5, 7).noted oneIn 26
                DRAGON_ARROWTIPS quantity IntRange(30, 40) oneIn 51
                MAGIC_LONGBOW_U quantity 1 oneIn 255

            }

            Tertiary {
                SCROLL_BOX_ELITE quantity 1 oneIn 160
            }
        }
    }
}
