package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.near_reality.scripts.npc.drops.table.tables.herb.HerbDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.AllotmentSeedDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.RareSeedDropTable
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

class CaveHorrorDroptable : NPCDropTableScript() {

    init {
        npcs(CAVE_HORROR, CAVE_HORROR_1048, CAVE_HORROR_1049, CAVE_HORROR_1050, CAVE_HORROR_1051)

        buildTable {
            Always {
                BIG_BONES quantity 1 rarity always
            }
            Main(128) {
                // Weapons and armour
                MITHRIL_AXE quantity 1 rarity 3
                RUNE_DAGGER quantity 1 rarity 1
                ADAMANT_FULL_HELM quantity 1 rarity 1
                MITHRIL_KITESHIELD quantity 1 rarity 1
                BLACK_MASK_10 quantity 1 rarity 1
                // Runes
                NATURE_RUNE quantity 6 rarity 6
                NATURE_RUNE quantity 4 rarity 5
                NATURE_RUNE quantity 3 rarity 1
                // Herbs
                chance(13) roll HerbDropTable
                // Seeds
                chance(18) roll RareSeedDropTable
                chance(15) roll AllotmentSeedDropTable
                // Coins
                COINS_995 quantity 44 rarity 28
                COINS_995 quantity 132 rarity 12
                COINS_995 quantity 440 rarity 1
                // Other
                LIMPWURT_ROOT quantity 1 rarity 7
                TEAK_LOGS quantity 4.noted rarity 7
                MAHOGANY_LOGS quantity 2 rarity 3
                // Gem drop table
                chance(5) roll GemDropTable
            }
            Tertiary {
                ENSOULED_HORROR_HEAD quantity 1 oneIn 30
                LONG_BONE quantity 1 oneIn 200
                CURVED_BONE quantity 1 oneIn 3012
            }
        }
    }
}
