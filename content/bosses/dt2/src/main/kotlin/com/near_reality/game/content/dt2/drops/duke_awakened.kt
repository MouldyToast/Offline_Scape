package com.near_reality.game.content.dt2.drops

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.content.util.hasKilledDukeAwakened
import com.zenyte.game.content.util.hasReceivedIceQuartz
import com.zenyte.game.content.util.playerHasKilledAllAwakenedBossesOnce
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.util.Utils
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class DukeAwakenedDroptable : NPCDropTableScript() {

    object DukeUniques : StandaloneDropTableBuilder({
        limit = 100
        static {
            /* 1/8 -> 1/200, 3/200 */
            CHROMIUM_INGOT quantity 1 rarity 46
            EYE_OF_THE_DUKE quantity 1 rarity 12
            MAGUS_ICON quantity 1 rarity 24
            VIRTUS_MASK quantity 1 rarity 6
            VIRTUS_ROBE_LEGS quantity 1 rarity 6
            VIRTUS_ROBE_TOP quantity 1 rarity 6
        }
    })

    init {
        npcs(DUKE_SUCELLUS_12195)

        onDeath {
            val baseRate = 3
            killer.hasKilledDukeAwakened = true
            npc.dropDukeItem(killer, Item(ARDERMUSCA_POISON, 2))

            if (Utils.random(350) == 0) {
                npc.dropDukeItem(killer, Item(MAGUS_VESTIGE, 1))
            }

            if (Utils.random(99) < baseRate) {
                rollTable(killer, Standalone, DukeUniques.staticTable).forEach {
                    npc.dropDukeItem(killer, Item(it.id, 1))
                }
                rollStaticTableAndDrop(killer, Tertiary)
            }
            if (Utils.random(25) == 0) {
                npc.dropDukeItem(killer, Item(AWAKENERS_ORB))
            }
            if (Utils.random(50) == 0) {
                npc.dropDukeItem(killer, Item(ICE_QUARTZ))
                killer.hasReceivedIceQuartz = true
            }
            if (Utils.random(5) == 0) {
                npc.dropDukeItem(killer, Item(TUNA_POTATO, (3..4).random()))
                npc.dropDukeItem(killer, Item(PRAYER_POTION3, 1))
                npc.dropDukeItem(killer, Item(SUPER_COMBAT_POTION2, 1))
            } else {
                rollStaticDukeTableAndDrop(killer, Main)
            }
            if (killer.playerHasKilledAllAwakenedBossesOnce()) {
                if (!killer.containsItem(ANCIENT_BLOOD_ORNAMENT_KIT))
                    npc.dropDukeItem(killer, Item(ANCIENT_BLOOD_ORNAMENT_KIT, 1))
            }

        }

        appendDrop(DisplayedDrop(CHROMIUM_INGOT, 1, 1, 72.46))
        appendDrop(DisplayedDrop(MAGUS_VESTIGE, 1, 1, 350.00))
        appendDrop(DisplayedDrop(EYE_OF_THE_DUKE, 1, 1, 277.78))
        appendDrop(DisplayedDrop(MAGUS_ICON, 1, 1, 138.89))
        appendDrop(DisplayedDrop(VIRTUS_MASK, 1, 1, 555.56))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_TOP, 1, 1, 555.56))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_LEGS, 1, 1, 555.56))

        appendDrop(DisplayedDrop(AWAKENERS_ORB, 1, 1, 25.00))
        appendDrop(DisplayedDrop(FROZEN_TABLET, 1, 1, 7.00))
        appendDrop(DisplayedDrop(ICE_QUARTZ, 1, 1, 50.00))
        appendDrop(DisplayedDrop(TUNA_POTATO, 3, 4, 5.00))
        put(TUNA_POTATO, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(PRAYER_POTION3, 1, 1, 5.00))
        put(PRAYER_POTION3, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(SUPER_COMBAT_POTION2, 1, 1, 5.00))
        put(SUPER_COMBAT_POTION2, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))


        /* 3/100 awakened */

        buildTable(100) {
            Main {
                //50
                COAL quantity 260.noted rarity 8
                ADAMANTITE_ORE quantity 90.noted rarity 8
                RUNE_JAVELIN_HEADS quantity 36 rarity 8
                DRAGON_JAVELIN_HEADS quantity 36 rarity 8
                UNCUT_RUBY quantity 40.noted rarity 5
                UNCUT_DIAMOND quantity 40.noted rarity 5
                RUNITE_ORE quantity 26.noted rarity 2
                DRAGON_DART_TIP quantity 175 rarity 2
                PURE_ESSENCE quantity 270.noted rarity 2
                IRON_ORE quantity 75.noted rarity 2

                //20
                SILVER_ORE quantity 75.noted rarity 1
                MITHRIL_ORE quantity 68.noted rarity 1
                SAPPHIRE quantity 26.noted rarity 2
                EMERALD quantity 26.noted rarity 2
                RUBY quantity 26.noted rarity 1
                RAW_SHARK quantity 170.noted rarity 1
                RUNE_FULL_HELM quantity 2.noted rarity 4
                LAVA_BATTLESTAFF quantity 2.noted rarity 4
                RUNE_HALBERD quantity 2.noted rarity 4

                //30
                LAVA_RUNE quantity 350 rarity 8
                BLOOD_RUNE quantity 350 rarity 8
                SOUL_RUNE quantity 450 rarity 2
                BRONZE_JAVELIN quantity 62 rarity 2
                MITHRIL_JAVELIN quantity 62 rarity 2
                ADAMANT_JAVELIN quantity 62 rarity 2
                ONYX_BOLTS_E quantity 65 rarity 2
                MIND_RUNE quantity 220 rarity 2
                FIRE_RUNE quantity 220 rarity 2
            }
            Tertiary {
                SCROLL_BOX_EASY quantity 1 oneIn 60
                SCROLL_BOX_MEDIUM quantity 1 oneIn 60
                SCROLL_BOX_HARD quantity 1 oneIn 40
                SCROLL_BOX_ELITE quantity 1 oneIn 25
                BARON quantity 1 oneIn 1000
            }
        }
    }
}
