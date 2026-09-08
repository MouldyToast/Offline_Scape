package com.near_reality.scripts.npc.drops.table.tables.seed

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ids.*

object TreeHerbSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        RANARR_SEED quantity 1 rarity 30
        SNAPDRAGON_SEED quantity 1 rarity 28
        TORSTOL_SEED quantity 1 rarity 22
        WATERMELON_SEED quantity 15 rarity 21
        WILLOW_SEED quantity 1 rarity 20
        MAHOGANY_SEED quantity 1 rarity 18
        MAPLE_SEED quantity 1 rarity 18
        TEAK_SEED quantity 1 rarity 18
        YEW_SEED quantity 1 rarity 18
        PAPAYA_TREE_SEED quantity 1 rarity 14
        MAGIC_SEED quantity 1 rarity 11
        PALM_TREE_SEED quantity 1 rarity 10
        SPIRIT_SEED quantity 1 rarity 4
        DRAGONFRUIT_TREE_SEED quantity 1 rarity 6
        CELASTRUS_SEED quantity 1 rarity 4
        REDWOOD_TREE_SEED quantity 1 rarity 4
    }
})
