package org.jesse.scripts.npc.drops.table.tables.seed

import org.jesse.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import org.jesse.game.item.ids.*

object RareSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        TOADFLAX_SEED quantity 1 rarity 47
        IRIT_SEED quantity 1 rarity 32
        BELLADONNA_SEED quantity 1 rarity 31
        AVANTOE_SEED quantity 1 rarity 22
        POISON_IVY_SEED quantity 1 rarity 22
        CACTUS_SEED quantity 1 rarity 21
        KWUARM_SEED quantity 1 rarity 15
        POTATO_CACTUS_SEED quantity 1 rarity 15
        SNAPDRAGON_SEED quantity 1 rarity 10
        CADANTINE_SEED quantity 1 rarity 7
        LANTADYME_SEED quantity 1 rarity 5
        SNAPE_GRASS_SEED quantity 3 rarity 4
        DWARF_WEED_SEED quantity 1 rarity 3
        TORSTOL_SEED quantity 1 rarity 2
    }
})
