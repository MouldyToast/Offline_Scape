package org.jesse.game.content.origins.nezikcheneds

import org.jesse.game.world.entity.player.dailyMysteryBox
import org.jesse.game.world.entity.player.extraDailyMysteryBox
import org.jesse.scripts.npc.drops.table.noted
import org.jesse.scripts.npc.drops.table.tables.rare.MegaRareDropTable
import org.jesse.scripts.npc.drops.table.tables.rare.RareDropTable
import org.jesse.scripts.npc.drops.table.tables.seed.TreeHerbSeedDropTable
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.player.privilege.MemberRank
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

class NezikchenedsDroptable : NPCDropTableScript() {

    init {
        npcs(NEZIKCHENED_6379)

        buildTable {
            Main(4000) {
                /* 1 in 20 drops (3600 pts)*/
                BLOOD_RUNE quantity 10..50 rarity 200
                DEATH_RUNE quantity 20..100 rarity 200
                YEW_LOGS quantity (20..40).noted rarity 200
                UNCUT_EMERALD quantity (5..15).noted rarity 200
                UNCUT_RUBY quantity (3..10).noted rarity 200

                UNCUT_DIAMOND quantity (2..7).noted rarity 200
                PURE_ESSENCE quantity (500..1000).noted rarity 200
                ADAMANTITE_BAR quantity (5..15).noted rarity 200
                RUNITE_BAR quantity (1..3).noted rarity 200
                MITHRIL_BAR quantity (7..20).noted rarity 200

                GRIMY_TORSTOL quantity (4..7).noted rarity 200
                GRIMY_RANARR_WEED quantity (7..11).noted rarity 200
                DRAGON_BONES quantity (2..5).noted rarity 200
                RUNE_PLATELEGS quantity 1.noted rarity 200
                RUNE_PLATEBODY quantity 1.noted rarity 200

                RUNE_FULL_HELM quantity 1.noted rarity 200
                BLACK_DRAGONHIDE quantity (2..3).noted rarity 200
                RED_DRAGONHIDE quantity (4..5).noted rarity 200

                /* 1 in 40 drops (300pts) */
                CRYSTAL_KEY quantity 1 rarity 100
                TOOTH_HALF_OF_KEY quantity 1 rarity 100
                LOOP_HALF_OF_KEY quantity 1 rarity 100

                /* 1 in 100 drops (80pts)*/
                MAGIC_SEED quantity (1..3) rarity 40
                YEW_SEED quantity (2..5) rarity 40

                /* 1 in 1000 drops (8 pts) */
                DRACONIC_VISAGE quantity 1 rarity 4 announce everywhere
                DRAGON_SPEAR quantity 1 rarity 4


                /* 1 in 4000 drops (6pts) */
                SKILLING_MYSTERY_BOX quantity 1 rarity 1
                OSNR_MYSTERY_BOX quantity 1 rarity 1 announce everywhere
            }
        }
    }
}
