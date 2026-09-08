package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.zenyte.game.item.ItemId
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.BROKEN_ZOMBIE_AXE
import com.zenyte.game.item.ItemId.BROKEN_ZOMBIE_HELMET
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.COSMIC_RUNE
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.EYE_OF_NEWT
import com.zenyte.game.item.ItemId.FISHING_BAIT
import com.zenyte.game.item.ItemId.GRIMY_AVANTOE
import com.zenyte.game.item.ItemId.GRIMY_CADANTINE
import com.zenyte.game.item.ItemId.GRIMY_DWARF_WEED
import com.zenyte.game.item.ItemId.GRIMY_GUAM_LEAF
import com.zenyte.game.item.ItemId.GRIMY_HARRALANDER
import com.zenyte.game.item.ItemId.GRIMY_IRIT_LEAF
import com.zenyte.game.item.ItemId.GRIMY_KWUARM
import com.zenyte.game.item.ItemId.GRIMY_LANTADYME
import com.zenyte.game.item.ItemId.GRIMY_MARRENTILL
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.GRIMY_TARROMIN
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.OAK_PLANK
import com.zenyte.game.item.ItemId.PLANK
import com.zenyte.game.item.ItemId.PURE_ESSENCE_NOTED
import com.zenyte.game.item.ItemId.RUNE_ARROW
import com.zenyte.game.item.ItemId.RUNE_KITESHIELD
import com.zenyte.game.item.ItemId.RUNE_MACE
import com.zenyte.game.item.ItemId.SCROLL_BOX_HARD
import com.zenyte.game.item.ItemId.TEAK_PLANK
import com.zenyte.game.item.ItemId.ZOMBIE_CHAMPION_SCROLL

class ArmouredZombiesDroptable : NPCDropTableScript() {

    init {
        npcs(ARMOURED_ZOMBIE, ARMOURED_ZOMBIE_12721, ARMOURED_ZOMBIE_12722, ARMOURED_ZOMBIE_12723, ARMOURED_ZOMBIE_12724, ARMOURED_ZOMBIE_12725,
                ARMOURED_ZOMBIE_12726, ARMOURED_ZOMBIE_12727, ARMOURED_ZOMBIE_12728, ARMOURED_ZOMBIE_12729, ARMOURED_ZOMBIE_12730, ARMOURED_ZOMBIE_12731,
                ARMOURED_ZOMBIE_12732, ARMOURED_ZOMBIE_12733, ARMOURED_ZOMBIE_12734, ARMOURED_ZOMBIE_12735, ARMOURED_ZOMBIE_12736, ARMOURED_ZOMBIE_12737,
                ARMOURED_ZOMBIE_12738, ARMOURED_ZOMBIE_12739, ARMOURED_ZOMBIE_12740, ARMOURED_ZOMBIE_12741, ARMOURED_ZOMBIE_12742, ARMOURED_ZOMBIE_12743,
                ARMOURED_ZOMBIE_12744, ARMOURED_ZOMBIE_12745, ARMOURED_ZOMBIE_12746, ARMOURED_ZOMBIE_12747, ARMOURED_ZOMBIE_12748, ARMOURED_ZOMBIE_12749,
                ARMOURED_ZOMBIE_12755, ARMOURED_ZOMBIE_12756, ARMOURED_ZOMBIE_12757, ARMOURED_ZOMBIE_12758, ARMOURED_ZOMBIE_12759, ARMOURED_ZOMBIE_12760,
                ARMOURED_ZOMBIE_12761, ARMOURED_ZOMBIE_12762, ARMOURED_ZOMBIE_12763, ARMOURED_ZOMBIE_12764, ARMOURED_ZOMBIE_14113, ARMOURED_ZOMBIE_14114,
                ARMOURED_ZOMBIE_14115, ARMOURED_ZOMBIE_14116, ARMOURED_ZOMBIE_14117, ARMOURED_ZOMBIE_14118, ARMOURED_ZOMBIE_14119, ARMOURED_ZOMBIE_14120,
                ARMOURED_ZOMBIE_14121, ARMOURED_ZOMBIE_14122)

        buildTable {
            Always {
                ItemId.BONES quantity 1 rarity always
            }
            Main(248) {
                // Runes & ammo
                PURE_ESSENCE_NOTED quantity (20..50) oneIn 11   // 20-50
                RUNE_ARROW quantity 12 oneIn 16   // 12
                BLOOD_RUNE quantity (6..14) oneIn 32   // 6-14
                COSMIC_RUNE quantity (15..30) oneIn 42   // 15-30
                NATURE_RUNE quantity (6..16) oneIn 64   // 6-16
                DEATH_RUNE quantity (6..14) oneIn 128   // 6-14
                CHAOS_RUNE quantity (15..30) oneIn 128   // 15-30
                // Herbs
                GRIMY_GUAM_LEAF quantity 1 oneIn 11     // 1
                GRIMY_MARRENTILL quantity 1 oneIn 15  // 1
                GRIMY_TARROMIN quantity 1 oneIn 20   // 1
                GRIMY_HARRALANDER quantity 1 oneIn 26 // 1
                GRIMY_RANARR_WEED quantity 1 oneIn 33 // 1
                GRIMY_IRIT_LEAF quantity 1 oneIn 45   // 1
                GRIMY_AVANTOE quantity 1 oneIn 60   // 1
                GRIMY_KWUARM quantity 1 oneIn 72   // 1
                GRIMY_CADANTINE quantity 1 oneIn 91   // 1
                GRIMY_LANTADYME quantity 1 oneIn 121  // 1
                GRIMY_DWARF_WEED quantity 1 oneIn 121 // 1
                // Other
                COINS_995 quantity (200..600) oneIn 4  // 200-600
                OAK_PLANK quantity 6.noted oneIn 21  // 200-600
                PLANK quantity (12).noted oneIn 26  // 12
                COINS_995 quantity (20..30) oneIn 44  // 20-30
                RUNE_MACE quantity 1 oneIn 43  // 1
                TEAK_PLANK quantity 3.noted oneIn 64  // 3
                EYE_OF_NEWT quantity (4..8).noted oneIn 128  // 4-8
                RUNE_KITESHIELD quantity 1 oneIn 128  // 1
                FISHING_BAIT quantity 6 oneIn 128  // 6

                // Rare
                chance(9) roll RareDropTable          // 248
            }
            Tertiary {
                BROKEN_ZOMBIE_AXE quantity 1 oneIn 600 announce everywhere
                BROKEN_ZOMBIE_HELMET quantity 1 oneIn 600 announce everywhere
                ZOMBIE_CHAMPION_SCROLL quantity 1 oneIn 5000
                SCROLL_BOX_HARD quantity 1 oneIn 13
            }
        }
    }
}
