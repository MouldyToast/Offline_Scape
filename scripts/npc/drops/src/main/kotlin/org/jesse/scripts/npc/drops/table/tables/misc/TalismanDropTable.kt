package org.jesse.scripts.npc.drops.table.tables.misc

import org.jesse.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import org.jesse.game.item.ids.*

object TalismanDropTable : StandaloneDropTableBuilder({
    limit = 70
    static {
        AIR_TALISMAN quantity 1 rarity 10
        BODY_TALISMAN quantity 1 rarity 10
        EARTH_TALISMAN quantity 1 rarity 10
        FIRE_TALISMAN quantity 1 rarity 10
        MIND_TALISMAN quantity 1 rarity 10
        WATER_TALISMAN quantity 1 rarity 10
        COSMIC_TALISMAN quantity 1 rarity 4
        CHAOS_TALISMAN quantity 1 rarity 3
        NATURE_TALISMAN quantity 1 rarity 3
    }
})
