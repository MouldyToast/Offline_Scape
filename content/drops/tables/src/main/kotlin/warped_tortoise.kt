package org.jesse.game.content

import org.jesse.scripts.npc.drops.table.always
import org.jesse.scripts.npc.drops.table.noted
import org.jesse.scripts.npc.drops.table.tables.rare.RareDropTable
import org.jesse.game.item.ids.*
import org.jesse.game.item.ids.ADAMANTITE_ORE
import org.jesse.game.item.ids.COAL
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

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
