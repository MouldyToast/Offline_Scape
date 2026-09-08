package com.near_reality.game.content.origins.di

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ItemId
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop
import com.zenyte.game.item.ItemId.AHRIMS_HOOD
import com.zenyte.game.item.ItemId.AHRIMS_ROBESKIRT
import com.zenyte.game.item.ItemId.AHRIMS_ROBETOP
import com.zenyte.game.item.ItemId.AHRIMS_STAFF
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.BOOK_OF_THE_DEAD
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.LOOP_HALF_OF_KEY
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.TOOTH_HALF_OF_KEY

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
