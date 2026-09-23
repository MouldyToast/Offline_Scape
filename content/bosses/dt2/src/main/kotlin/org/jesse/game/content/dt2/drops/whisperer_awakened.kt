package org.jesse.game.content.dt2.drops

import org.jesse.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import org.jesse.scripts.npc.drops.table.noted
import org.jesse.game.content.util.hasKilledWhispererAwakened
import org.jesse.game.content.util.hasReceivedShadowQuartz
import org.jesse.game.content.util.playerHasKilledAllAwakenedBossesOnce
import org.jesse.game.item.Item
import org.jesse.game.util.Utils
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.game.item.ids.ADAMANTITE_ORE
import org.jesse.game.item.ids.COAL
import org.jesse.game.item.ids.GOLD_ORE
import org.jesse.game.item.ids.IRON_ORE
import org.jesse.game.item.ids.MITHRIL_ORE
import org.jesse.game.item.ids.RUNITE_ORE
import org.jesse.game.item.ids.WISP
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.item.ids.MANTA_RAY
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class WhispererAwakenedDroptable : NPCDropTableScript() {

    object WhispererUniques : StandaloneDropTableBuilder({
        limit = 100
        static {
            CHROMIUM_INGOT quantity 1 rarity 46
            SIRENS_STAFF quantity 1 rarity 6
            VIRTUS_MASK quantity 1 rarity 6
            VIRTUS_ROBE_TOP quantity 1 rarity 6
            VIRTUS_ROBE_BOTTOM quantity 1 rarity 6
        }
    })

    init {
        npcs(THE_WHISPERER_12206)

        onDeath {

            killer.hasKilledWhispererAwakened = true


            if (Utils.random(511) == 0) {
                npc.dropItemAtKiller(killer, Item(BELLATOR_VESTIGE, 1))
            }

            if (Utils.random(63) < 3) {
                rollTable(killer, Standalone, WhispererUniques.staticTable).forEach {
                    npc.dropItemAtKiller(killer, Item(it.id, 1))
                }
            }

            else if (Utils.random(33) == 0) { 
                npc.dropItemAtKiller(killer, Item(AWAKENERS_ORB))
            } else if (Utils.random(199) == 0) {
                npc.dropItemAtKiller(killer, Item(SHADOW_QUARTZ))
                killer.hasReceivedShadowQuartz = true
            }

            else if (Utils.random(4) == 0) {
                npc.dropItemAtKiller(killer, Item(ANCIENT_BREW_4, 2))
                npc.dropItemAtKiller(killer, Item(PRAYER_POTION3, 1))
                npc.dropItemAtKiller(killer, Item(MANTA_RAY, (3..4).random()))
            }

            else {
                rollStaticTableAndDropBelowPlayer(killer, Main)
            }


            if (Utils.random(500) == 0) {
                npc.dropItemAtKiller(killer, Item(WISP))
            }
            if (Utils.random(39) == 0) { 
                npc.dropItemAtKiller(killer, Item(CLUE_SCROLL))
            }
            if (killer.playerHasKilledAllAwakenedBossesOnce()) {
                if (!killer.containsItem(ANCIENT_BLOOD_ORNAMENT_KIT))
                    npc.dropItemAtKiller(killer, Item(ANCIENT_BLOOD_ORNAMENT_KIT, 1))
            }
        }



        appendDrop(DisplayedDrop(BELLATOR_VESTIGE, 1, 1, 512.00))
        appendDrop(DisplayedDrop(AWAKENERS_ORB, 1, 1, 34.00))
        appendDrop(DisplayedDrop(SIRENIC_TABLET, 1, 1, 25.00))
        appendDrop(DisplayedDrop(SHADOW_QUARTZ, 1, 1, 200.00))
        appendDrop(DisplayedDrop(ANCIENT_BREW_4, 2, 2, 5.00))
        appendDrop(DisplayedDrop(PRAYER_POTION3, 1, 1, 5.00))
        appendDrop(DisplayedDrop(MANTA_RAY, 3, 4, 5.00))
        appendDrop(DisplayedDrop(WISP, 1, 1, 500.00))


        appendDrop(DisplayedDrop(BRONZE_LONGSWORD, 16, 24, 100.00))
        appendDrop(DisplayedDrop(MITHRIL_LONGSWORD, 7, 10, 100.00))
        appendDrop(DisplayedDrop(ADAMANT_LONGSWORD, 9, 14, 100.00))
        appendDrop(DisplayedDrop(BATTLESTAFF, 70, 105, 200.00))
        appendDrop(DisplayedDrop(DRAGON_PLATESKIRT, 7, 10, 100.00))

        appendDrop(DisplayedDrop(PURE_ESSENCE, 280, 420, 100.00))
        appendDrop(DisplayedDrop(IRON_ORE, 88, 133, 100.00))
        appendDrop(DisplayedDrop(COAL, 303, 455, 800.00))
        appendDrop(DisplayedDrop(GOLD_ORE, 88, 133, 100.00))
        appendDrop(DisplayedDrop(MITHRIL_ORE, 88, 133, 100.00))
        appendDrop(DisplayedDrop(ADAMANTITE_ORE, 105, 157, 800.00))
        appendDrop(DisplayedDrop(RUNITE_ORE, 42, 63, 200.00))
        appendDrop(DisplayedDrop(SAPPHIRE, 39, 59, 100.00))
        appendDrop(DisplayedDrop(EMERALD, 39, 59, 100.00))
        appendDrop(DisplayedDrop(RUBY, 39, 59, 100.00))
        appendDrop(DisplayedDrop(UNCUT_RUBY, 58, 87, 500.00))
        appendDrop(DisplayedDrop(UNCUT_DIAMOND, 58, 87, 500.00))
        appendDrop(DisplayedDrop(DRAGON_JAVELIN_TIPS, 84, 126, 800.00))
        appendDrop(DisplayedDrop(RUNITE_BOLTS_UNF, 84, 126, 800.00))
        appendDrop(DisplayedDrop(RAW_MONKFISH, 700, 1050, 100.00))

        appendDrop(DisplayedDrop(WATER_RUNE, 280, 420, 100.00))
        appendDrop(DisplayedDrop(STEAM_RUNE, 466, 700, 800.00))
        appendDrop(DisplayedDrop(CHAOS_RUNE, 140, 210, 100.00))
        appendDrop(DisplayedDrop(DEATH_RUNE, 466, 700, 800.00))
        appendDrop(DisplayedDrop(SOUL_RUNE, 933, 1400, 200.00))

        appendDrop(DisplayedDrop(VIRTUS_MASK, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_TOP, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_BOTTOM, 1, 1, 1600.00))


        buildTable(100) {
            Main {
                BRONZE_LONGSWORD quantity 16.noted rarity 1
                MITHRIL_LONGSWORD quantity 7.noted rarity 1
                ADAMANT_LONGSWORD quantity 9.noted rarity 1
                BATTLESTAFF quantity 70.noted rarity 2
                DRAGON_PLATESKIRT quantity 7 rarity 1

                PURE_ESSENCE quantity 280.noted rarity 1
                IRON_ORE quantity 88.noted rarity 1
                COAL quantity 303.noted rarity 8
                GOLD_ORE quantity 88.noted rarity 1
                MITHRIL_ORE quantity 88.noted rarity 1
                ADAMANTITE_ORE quantity 105.noted rarity 8
                RUNITE_ORE quantity 42.noted rarity 2
                SAPPHIRE quantity 39.noted rarity 1
                EMERALD quantity 39.noted rarity 1
                RUBY quantity 39.noted rarity 1
                UNCUT_RUBY quantity 58.noted rarity 5
                UNCUT_DIAMOND quantity 58.noted rarity 5
                DRAGON_JAVELIN_TIPS quantity 84 rarity 8
                RUNITE_BOLTS_UNF quantity 84 rarity 8
                RAW_MONKFISH quantity 700.noted rarity 1

                WATER_RUNE quantity 280 rarity 1
                STEAM_RUNE quantity 466 rarity 8
                CHAOS_RUNE quantity 140 rarity 1
                DEATH_RUNE quantity 466 rarity 8
                SOUL_RUNE quantity 933 rarity 2
            }
            Tertiary {
                WISP quantity 1 oneIn 2000
                CLUE_SCROLL quantity 1 oneIn 40
            }
        }
    }
}
