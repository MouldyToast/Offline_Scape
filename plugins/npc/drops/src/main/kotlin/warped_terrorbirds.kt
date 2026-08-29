package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.NpcId.WARPED_TERRORBIRD
import com.zenyte.game.world.entity.npc.NpcId.WARPED_TERRORBIRD_12492
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class WarpedTerrorbirdsDroptable : NPCDropTableScript() {

    init {
        npcs(WARPED_TERRORBIRD, WARPED_TERRORBIRD_12492, WARPED_TERRORBIRD_12493, WARPED_TERRORBIRD_12494,
            WARPED_TERRORBIRD_12495, WARPED_TERRORBIRD_12496, WARPED_TERRORBIRD_12497, WARPED_TERRORBIRD_12498,
            WARPED_TERRORBIRD_12499, WARPED_TERRORBIRD_12500, WARPED_TERRORBIRD_12501, WARPED_TERRORBIRD_12502,
            WARPED_TERRORBIRD_12503, WARPED_TERRORBIRD_12504)

        buildTable {
            Always {
                ItemId.BONES quantity 1 rarity always
            }
            Main(248) {
                // Weapons and Armour
                ADAMANT_WARHAMMER quantity 1 oneIn 32     // 32
                ADAMANT_PLATEBODY quantity 1 oneIn 32     // 32
                RUNE_BATTLEAXE quantity 1 oneIn 64     // 64
                RUNE_KITESHIELD quantity 1 oneIn 64     // 64
                RUNE_WARHAMMER quantity 1 oneIn 64     // 64
                // Runes
                AIR_RUNE quantity (80..120) oneIn 11   // 80-120
                EARTH_RUNE quantity (80..100) oneIn 11   // 80-100
                DEATH_RUNE quantity (15..20) oneIn 22   // 15-20
                LAW_RUNE quantity (15..20) oneIn 22   // 15-20
                SOUL_RUNE quantity (10..15) oneIn 22   // 10-15
                // Other
                COINS_995 quantity (600..800) oneIn 8  // 600-800
                RAW_SHARK quantity (3..7).noted oneIn 13  // 3-7
                SWAMP_TAR quantity (40..60) oneIn 13  // 40-60
                ItemId.FEATHER quantity (100..200) oneIn 13  // 100-200
                WEAPON_POISON quantity 1 oneIn 21     // 1
                DIAMOND_BOLT_TIPS quantity (24..32) oneIn 21  // 24-32
                CHOCOLATE_BOMB quantity (2..3) oneIn 21  // 2-3
                ItemId.ADAMANTITE_ORE quantity (3..5).noted oneIn 32  // 3-5
                // Rare
                chance(9) roll RareDropTable          // 248
            }
            Tertiary {
                WARPED_SCEPTRE_UNCHARGED quantity 1 oneIn 320 announce everywhere
                BRIMSTONE_KEY quantity 1 oneIn 82
                SCROLL_BOX_HARD quantity 1 oneIn 13
            }
        }
    }
}
