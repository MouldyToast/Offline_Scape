package org.jesse.game.content.origins.di

import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class DiAhrimDroptable : NPCDropTableScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.24.2025
         */

        npcs(DI_AHRIM_THE_BLIGHTED)

        buildTable {
            Main(denominator = 512) {
                COINS_995 quantity 500..1500 rarity 400
                BLOOD_RUNE quantity 10..25 rarity 25
                MIND_RUNE quantity 25..50 rarity 25
                DEATH_RUNE quantity 15..30 rarity 25
                CHAOS_RUNE quantity 20..40 rarity 25

                // the 12
                AHRIMS_HOOD quantity 1 rarity 2
                AHRIMS_ROBETOP quantity 1 rarity 2
                AHRIMS_ROBESKIRT quantity 1 rarity 2
                AHRIMS_STAFF quantity 1 rarity 2
                BOOK_OF_THE_DEAD quantity 1 rarity 1
                TOOTH_HALF_OF_KEY quantity 1 rarity 1
                LOOP_HALF_OF_KEY quantity 1 rarity 1
            }
        }
    }
}
