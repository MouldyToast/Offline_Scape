package com.near_reality.scripts.npc.drops.table.tables.seed

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ids.*

object GeneralSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        POTATO_SEED quantity 4 rarity 368
        ONION_SEED quantity 4 rarity 276
        CABBAGE_SEED quantity 4 rarity 184
        TOMATO_SEED quantity 3 rarity 92
        SWEETCORN_SEED quantity 3 rarity 46
        STRAWBERRY_SEED quantity 2 rarity 23
        WATERMELON_SEED quantity 2 rarity 11
        SNAPE_GRASS_SEED quantity 2 rarity 8
    }
})
