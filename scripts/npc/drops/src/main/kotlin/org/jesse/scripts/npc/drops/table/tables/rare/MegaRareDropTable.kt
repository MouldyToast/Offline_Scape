package org.jesse.scripts.npc.drops.table.tables.rare

import org.jesse.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import org.jesse.scripts.npc.drops.table.nothing
import org.jesse.game.item.ids.*

object MegaRareDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        RUNE_SPEAR quantity 1 rarity 8
        SHIELD_LEFT_HALF quantity 1 rarity 4
        DRAGON_SPEAR quantity 1 rarity 3
    }
})
