package com.near_reality.scripts.npc.drops.table.tables.seed

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ids.*

object AllotmentSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        POTATO_SEED quantity (1..4) rarity 64
        ONION_SEED quantity (1..3) rarity 32
        CABBAGE_SEED quantity (1..3) rarity 16
        TOMATO_SEED quantity (1..2) rarity 8
        SWEETCORN_SEED quantity (1..2) rarity 4
        STRAWBERRY_SEED quantity 1 rarity 2
        WATERMELON_SEED quantity 1 rarity 1
        SNAPE_GRASS_SEED quantity 1 rarity 1
    }
})

