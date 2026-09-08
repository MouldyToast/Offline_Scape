package com.near_reality.game.content.origins.nezikcheneds

import com.near_reality.game.world.entity.player.dailyMysteryBox
import com.near_reality.game.world.entity.player.extraDailyMysteryBox
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.rare.MegaRareDropTable
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.TreeHerbSeedDropTable
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.privilege.MemberRank
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
import com.zenyte.game.item.ItemId.ADAMANTITE_BAR
import com.zenyte.game.item.ItemId.BLACK_DRAGONHIDE
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.CRYSTAL_KEY
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.DRACONIC_VISAGE
import com.zenyte.game.item.ItemId.DRAGON_BONES
import com.zenyte.game.item.ItemId.DRAGON_SPEAR
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.GRIMY_TORSTOL
import com.zenyte.game.item.ItemId.LOOP_HALF_OF_KEY
import com.zenyte.game.item.ItemId.MAGIC_SEED
import com.zenyte.game.item.ItemId.MITHRIL_BAR
import com.zenyte.game.item.ItemId.OSNR_MYSTERY_BOX
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.RED_DRAGONHIDE
import com.zenyte.game.item.ItemId.RUNE_FULL_HELM
import com.zenyte.game.item.ItemId.RUNE_PLATEBODY
import com.zenyte.game.item.ItemId.RUNE_PLATELEGS
import com.zenyte.game.item.ItemId.RUNITE_BAR
import com.zenyte.game.item.ItemId.SKILLING_MYSTERY_BOX
import com.zenyte.game.item.ItemId.TOOTH_HALF_OF_KEY
import com.zenyte.game.item.ItemId.UNCUT_DIAMOND
import com.zenyte.game.item.ItemId.UNCUT_EMERALD
import com.zenyte.game.item.ItemId.UNCUT_RUBY
import com.zenyte.game.item.ItemId.YEW_LOGS
import com.zenyte.game.item.ItemId.YEW_SEED

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
