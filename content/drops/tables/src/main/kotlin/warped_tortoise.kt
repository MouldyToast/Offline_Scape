package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.zenyte.game.item.ids.*
import com.zenyte.game.item.ids.ADAMANTITE_ORE
import com.zenyte.game.item.ids.COAL
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class WarpedTortoiseDroptable : NPCDropTableScript() {

    init {
        npcs(WARPED_TORTOISE)

        buildTable {
            Always {
                BIG_BONES quantity 1 rarity always
            }
            Main(248) {
                // Weapons and Armour
                ADAMANT_AXE quantity 1 oneIn 32     // 32
                ADAMANT_PLATEBODY quantity 1 oneIn 32     // 32
                RUNE_PICKAXE quantity 1 oneIn 64     // 64
                RUNE_KITESHIELD quantity 1 oneIn 64     // 64
                RUNE_WARHAMMER quantity 1 oneIn 64     // 64
                // Runes
                EARTH_RUNE quantity (80..100) oneIn 11   // 80-100
                MUD_RUNE quantity (30..50) oneIn 13   // 30-50
                DEATH_RUNE quantity (15..20) oneIn 22   // 15-20
                // Other
                COINS_995 quantity (600..800) oneIn 6  // 600-800
                SWAMP_TAR quantity (40..60) oneIn 10  // 40-60
                CABBAGE quantity (20..40).noted oneIn 10  // 20-40
                COAL quantity (6..12).noted oneIn 21  // 6-12
                WEAPON_POISON quantity 1 oneIn 21     // 1
                PINEAPPLE quantity 1 oneIn 21     // 1
                TANGLED_TOADS_LEGS quantity (2..3) oneIn 21  // 2-3
                ADAMANTITE_ORE quantity (3..5).noted oneIn 32  // 3-5
                TORTOISE_SHELL quantity (1..3) oneIn 32  // 1-3
                PERFECT_SHELL quantity (1..3) oneIn 32  // 1-3
                // Rare
                chance(9) roll RareDropTable          // 248
            }
            Tertiary {
                WARPED_SCEPTRE_UNCHARGED quantity 1 oneIn 320 announce everywhere
                BRIMSTONE_KEY quantity 1 oneIn 76
                SCROLL_BOX_HARD quantity 1 oneIn 13
            }
        }
    }
}
