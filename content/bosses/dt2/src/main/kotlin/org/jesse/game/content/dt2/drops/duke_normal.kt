package org.jesse.game.content.dt2.drops

import org.jesse.game.content.dt2.npc.theduke.DukeSucellusEntity
import org.jesse.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import org.jesse.scripts.npc.drops.table.noted
import org.jesse.game.content.util.hasReceivedIceQuartz
import org.jesse.game.item.Item
import org.jesse.game.util.Utils
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.game.item.ids.ADAMANTITE_ORE
import org.jesse.game.item.ids.BARON
import org.jesse.game.item.ids.COAL
import org.jesse.game.item.ids.IRON_ORE
import org.jesse.game.item.ids.MITHRIL_ORE
import org.jesse.game.item.ids.RUNITE_ORE
import org.jesse.game.item.ids.SILVER_ORE
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class DukeNormalDroptable : NPCDropTableScript() {

    object DukeUniques : StandaloneDropTableBuilder({
        limit = 16
        static {
            /* 1/2 -> 1/200, 3/200 */
            CHROMIUM_INGOT quantity 1 rarity 8
            EYE_OF_THE_DUKE quantity 1 rarity 3
            MAGUS_ICON quantity 1 rarity 2
            VIRTUS_MASK quantity 1 rarity 1
            VIRTUS_ROBE_BOTTOM quantity 1 rarity 1
            VIRTUS_ROBE_TOP quantity 1 rarity 1
        }
    })

    init {
        npcs(DUKE_SUCELLUS_12191)

        onDeath {
            val duke = this.npc as DukeSucellusEntity
            val baseRate = 1

            if(baseRate > 0) {
                npc.dropDukeItem(killer, Item(ARDERMUSCA_POISON, 2))
                if(Utils.random(99) < baseRate) {
                    rollTable(killer, Standalone, DukeUniques.staticTable).forEach {
                        npc.dropDukeItem(killer, Item(it.id, 1))
                    }
                    rollStaticTableAndDrop(killer, Tertiary)
                } else if(Utils.random(85) == 0) {
                    npc.dropDukeItem(killer, Item(AWAKENERS_ORB))
                } else if(Utils.random(149) == 0) {
                    npc.dropDukeItem(killer, Item(ICE_QUARTZ))
                    killer.hasReceivedIceQuartz = true
                } else if(Utils.random(5) == 0) {
                    npc.dropDukeItem(killer, Item(TUNA_POTATO, (3..4).random()))
                    npc.dropDukeItem(killer, Item(PRAYER_POTION3, 1))
                    npc.dropDukeItem(killer, Item(SUPER_COMBAT_POTION2, 1))
                } else {
                    rollStaticDukeTableAndDrop(killer, Main)
                }
            }
        }

        appendDrop(DisplayedDrop(CHROMIUM_INGOT, 1, 1, 200.00))
        appendDrop(DisplayedDrop(EYE_OF_THE_DUKE, 1, 1, 533.33))
        appendDrop(DisplayedDrop(MAGUS_ICON, 1, 1, 800.00))
        appendDrop(DisplayedDrop(VIRTUS_MASK, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_TOP, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_BOTTOM, 1, 1, 1600.00))

        appendDrop(DisplayedDrop(AWAKENERS_ORB, 1, 1, 75.00))
        appendDrop(DisplayedDrop(ICE_QUARTZ, 1, 1, 150.00))
        appendDrop(DisplayedDrop(TUNA_POTATO, 3, 4, 5.00))
        put(TUNA_POTATO, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(PRAYER_POTION3, 1, 1, 5.00))
        put(PRAYER_POTION3, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(SUPER_COMBAT_POTION2, 1, 1, 5.00))
        put(SUPER_COMBAT_POTION2, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))


        /* 1/100 base || 3/100 awakened */

        buildTable(100) {
            Main {
                //50
                COAL quantity 130.noted rarity 8
                ADAMANTITE_ORE quantity 45.noted rarity 8
                RUNE_JAVELIN_TIPS quantity 24 rarity 8
                DRAGON_JAVELIN_TIPS quantity 24 rarity 8
                UNCUT_RUBY quantity 25.noted rarity 5
                UNCUT_DIAMOND quantity 25.noted rarity 5
                RUNITE_ORE quantity 18.noted rarity 2
                DRAGON_DART_TIP quantity 100 rarity 2
                PURE_ESSENCE quantity 120.noted rarity 2
                IRON_ORE quantity 38.noted rarity 2

                //20
                SILVER_ORE quantity 38.noted rarity 1
                MITHRIL_ORE quantity 38.noted rarity 1
                SAPPHIRE quantity 17.noted rarity 2
                EMERALD quantity 17.noted rarity 2
                RUBY quantity 17.noted rarity 1
                RAW_SHARK quantity 120.noted rarity 1
                RUNE_FULL_HELM quantity 1 rarity 4
                LAVA_BATTLESTAFF quantity 1 rarity 4
                RUNE_HALBERD quantity 1 rarity 4

                //30
                LAVA_RUNE quantity 200 rarity 8
                BLOOD_RUNE quantity 200 rarity 8
                SOUL_RUNE quantity 400 rarity 2
                BRONZE_JAVELIN quantity 42 rarity 2
                MITHRIL_JAVELIN quantity 42 rarity 2
                ADAMANT_JAVELIN quantity 42 rarity 2
                ONYX_BOLTS_E quantity 35 rarity 2
                MIND_RUNE quantity 120 rarity 2
                FIRE_RUNE quantity 120 rarity 2
            }
            Tertiary {
                SCROLL_BOX_EASY quantity 1 oneIn 160
                SCROLL_BOX_MEDIUM quantity 1 oneIn 160
                SCROLL_BOX_HARD quantity 1 oneIn 160
                SCROLL_BOX_ELITE quantity 1 oneIn 160
                BARON quantity 1 oneIn 2000
            }
        }
    }
}
