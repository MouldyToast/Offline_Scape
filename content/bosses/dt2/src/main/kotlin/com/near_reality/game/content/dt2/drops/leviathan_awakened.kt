package com.near_reality.game.content.dt2.drops

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.content.util.hasKilledLeviathanAwakened
import com.zenyte.game.content.util.hasReceivedSmokeQuartz
import com.zenyte.game.content.util.playerHasKilledAllAwakenedBossesOnce
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class LeviathanAwakenedDroptable : NPCDropTableScript() {

    object LeviathanUniques : StandaloneDropTableBuilder({
        limit = 24
        static {
            VENATOR_VESTIGE quantity 1 rarity 3
            CHROMIUM_INGOT quantity 1 rarity 9
            LEVIATHANS_LURE quantity 1 rarity 3
            VIRTUS_MASK quantity 1 rarity 1
            VIRTUS_ROBE_TOP quantity 1 rarity 1
            VIRTUS_ROBE_LEGS quantity 1 rarity 1
        }
    })

    init {
        // Define the NPC for the awakened Leviathan (e.g. THE_LEVIATHAN_AWAKENED is defined elsewhere)
        npcs(THE_LEVIATHAN_12215)

        onDeath {
            killer.hasKilledLeviathanAwakened = true

            // Roll 3/96 for the tradeable unique drop table
            if (Utils.random(95) < 3) {
                rollTable(killer, Standalone, LeviathanUniques.staticTable).forEach {
                    npc.dropItemAtKiller(killer, Item(it.id, 1))
                }
            }
            // Roll 1/53 for the Awakener's orb
            else if (Utils.random(52) == 0) {
                npc.dropItemAtKiller(killer, Item(AWAKENERS_ORB))
            }
            // Roll 1/200 for Smoke quartz
            else if (Utils.random(199) == 0) {
                npc.dropItemAtKiller(killer, Item(SMOKE_QUARTZ))
                killer.hasReceivedSmokeQuartz = true
            }
            // Roll 1/5 for the supply drop
            else if (Utils.random(4) == 0) {
                npc.dropItemAtKiller(killer, Item(PRAYER_POTION3, 1))
                npc.dropItemAtKiller(killer, Item(RANGING_POTION2, 1))
                npc.dropItemAtKiller(killer, Item(SEA_TURTLE, (3..4).random()))
            }
            // Otherwise roll from the standard drop table
            else {
                rollStaticTableAndDropBelowPlayer(killer, Main)
            }

            // Tertiary drops:
            if (Utils.random(500) == 0) {
                npc.dropItemAtKiller(killer, Item(ItemId.LILVIATHAN))
            }
            if (Utils.random(39) == 0) {
                npc.dropItemAtKiller(killer, Item(CLUE_SCROLL))
            }
            if (killer.playerHasKilledAllAwakenedBossesOnce()) {
                if (!killer.containsItem(ItemId.ANCIENT_BLOOD_ORNAMENT_KIT))
                    npc.dropItemAtKiller(killer, Item(ItemId.ANCIENT_BLOOD_ORNAMENT_KIT, 1))
            }
        }

        appendDrop(DisplayedDrop(AWAKENERS_ORB, 1, 1, 52.00))
        appendDrop(DisplayedDrop(SMOKE_QUARTZ, 1, 1, 199.00))
        appendDrop(DisplayedDrop(PRAYER_POTION3, 1, 1, 4.00))
        appendDrop(DisplayedDrop(RANGING_POTION2, 1, 1, 4.00))
        appendDrop(DisplayedDrop(SEA_TURTLE, 3, 4, 4.00))
        appendDrop(DisplayedDrop(ItemId.LILVIATHAN, 1, 1, 500.00))
        appendDrop(DisplayedDrop(CLUE_SCROLL, 1, 1, 39.00))

        appendDrop(DisplayedDrop(PURE_ESSENCE, 180, 270, 100.00))
        appendDrop(DisplayedDrop(ItemId.IRON_ORE, 57, 85, 100.00))
        appendDrop(DisplayedDrop(ItemId.SILVER_ORE, 57, 85, 100.00))
        appendDrop(DisplayedDrop(ItemId.COAL, 195, 292, 800.00))
        appendDrop(DisplayedDrop(ItemId.GOLD_ORE, 67, 101, 800.00))
        appendDrop(DisplayedDrop(ItemId.ADAMANTITE_ORE, 57, 85, 100.00))
        appendDrop(DisplayedDrop(ItemId.RUNITE_ORE, 27, 40, 200.00))
        appendDrop(DisplayedDrop(SAPPHIRE, 25, 38, 100.00))
        appendDrop(DisplayedDrop(EMERALD, 25, 38, 100.00))
        appendDrop(DisplayedDrop(RUBY, 25, 38, 100.00))
        appendDrop(DisplayedDrop(UNCUT_RUBY, 37, 56, 500.00))
        appendDrop(DisplayedDrop(UNCUT_DIAMOND, 37, 56, 500.00))
        appendDrop(DisplayedDrop(DRAGON_JAVELIN_HEADS, 54, 81, 800.00))
        appendDrop(DisplayedDrop(DRAGON_BOLTS_UNF, 150, 225, 200.00))
        appendDrop(DisplayedDrop(ONYX_BOLT_TIPS, 90, 135, 100.00))
        appendDrop(DisplayedDrop(RAW_MANTA_RAY, 180, 270, 100.00))
        appendDrop(DisplayedDrop(ANGLERFISH, 4, 6, 800.00))

        appendDrop(DisplayedDrop(BRONZE_ARROW, 63, 94, 100.00))
        appendDrop(DisplayedDrop(MITHRIL_ARROW, 63, 94, 100.00))
        appendDrop(DisplayedDrop(ADAMANT_ARROW, 63, 94, 100.00))
        appendDrop(DisplayedDrop(RUNE_ARROW, 54, 81, 800.00))
        appendDrop(DisplayedDrop(BODY_RUNE, 180, 270, 100.00))
        appendDrop(DisplayedDrop(EARTH_RUNE, 180, 270, 100.00))
        appendDrop(DisplayedDrop(SMOKE_RUNE, 300, 450, 800.00))
        appendDrop(DisplayedDrop(SOUL_RUNE, 600, 900, 200.00))

        appendDrop(DisplayedDrop(VIRTUS_MASK, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_TOP, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_LEGS, 1, 1, 1600.00))

        buildTable(100) {
            Main {
                PURE_ESSENCE quantity 180.noted rarity 1
                ItemId.IRON_ORE quantity 57.noted rarity 1
                ItemId.SILVER_ORE quantity 57.noted rarity 1
                ItemId.COAL quantity 195.noted rarity 8
                ItemId.GOLD_ORE quantity 67.noted rarity 8
                ItemId.ADAMANTITE_ORE quantity 57.noted rarity 1
                ItemId.RUNITE_ORE quantity 27.noted rarity 2
                SAPPHIRE quantity 25.noted rarity 1
                EMERALD quantity 25.noted rarity 1
                RUBY quantity 25.noted rarity 1
                UNCUT_RUBY quantity 37.noted rarity 5
                UNCUT_DIAMOND quantity 37.noted rarity 5
                DRAGON_JAVELIN_HEADS quantity 54 rarity 8
                DRAGON_BOLTS_UNF quantity 150 rarity 2
                ONYX_BOLT_TIPS quantity 90 rarity 1
                RAW_MANTA_RAY quantity 180.noted rarity 1
                ANGLERFISH quantity 4.noted rarity 8

                BRONZE_ARROW quantity 63 rarity 1
                MITHRIL_ARROW quantity 63 rarity 1
                ADAMANT_ARROW quantity 63 rarity 1
                RUNE_ARROW quantity 54 rarity 8
                BODY_RUNE quantity 180 rarity 1
                EARTH_RUNE quantity 180 rarity 1
                SMOKE_RUNE quantity 300 rarity 8
                SOUL_RUNE quantity 600 rarity 2
            }
            Tertiary {
                ItemId.LILVIATHAN quantity 1 oneIn 2500
                CLUE_SCROLL quantity 1 oneIn 40
            }
        }

        // ------------------------------
        // Unique drop table for Leviathan (awakened)
        // Using a higher table limit so that when rolled (at 3/96 chance) the effective rates are increased
        // Desired internal probabilities (when the table is rolled):
        // • Venator vestige: 1/24 → rarity = 72/24 = 3
        // • Chromium ingot: 1/8  → rarity = 72/8 = 9
        // • Leviathan's lure: 1/24 → rarity = 3
        // • Each Virtus item: 1/72 → rarity = 72/72 = 1
        // ------------------------------
    }
}
