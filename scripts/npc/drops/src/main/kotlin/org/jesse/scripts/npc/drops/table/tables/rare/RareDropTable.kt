package org.jesse.scripts.npc.drops.table.tables.rare

import org.jesse.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import org.jesse.scripts.npc.drops.table.noted
import org.jesse.scripts.npc.drops.table.tables.gem.GemDropTable
import org.jesse.game.item.ids.*

object RareDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        // Runes and ammunition
        NATURE_RUNE quantity 67 rarity 3
        ADAMANT_JAVELIN quantity 20 rarity 2
        DEATH_RUNE quantity 45 rarity 2
        LAW_RUNE quantity 45 rarity 2
        RUNE_ARROW quantity 42 rarity 2
        STEEL_ARROW quantity 150 rarity 2
        // Weapons and armour
        RUNE_2H_SWORD quantity 1 rarity 3
        RUNE_BATTLEAXE quantity 1 rarity 3
        RUNE_SQ_SHIELD quantity 1 rarity 2
        DRAGON_MED_HELM quantity 1 rarity 1
        RUNE_KITESHIELD quantity 1 rarity 1
        // Other
        COINS_995 quantity 3000 rarity 21
        LOOP_HALF_OF_KEY quantity 1 rarity 20
        TOOTH_HALF_OF_KEY quantity 1 rarity 20
        RUNITE_BAR quantity 1 rarity 5
        DRAGONSTONE quantity 1 rarity 2
        SILVER_ORE quantity 100.noted rarity 2
        // Sub-tables
        chance(20) roll GemDropTable
        chance(15) roll MegaRareDropTable
    }
})
