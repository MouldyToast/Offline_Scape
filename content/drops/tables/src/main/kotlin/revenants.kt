package org.jesse.game.content

import org.jesse.scripts.npc.drops.table.noted
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.game.item.ids.COAL
import org.jesse.game.item.ids.RUNITE_ORE
import org.jesse.game.item.ids.MANTA_RAY
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class RevenantsDroptable : NPCDropTableScript() {

    init {
        /**
         * This only handles the common/uncommon drops, all uniques are handled through DropProcessor implementations due to
         * significantly varied drop rates.
         */

        npcs(REVENANT_ORK, REVENANT_IMP, REVENANT_DEMON, REVENANT_DRAGON, REVENANT_DARK_BEAST, REVENANT_KNIGHT, REVENANT_HOBGOBLIN, REVENANT_HELLHOUND, REVENANT_GOBLIN, REVENANT_PYREFIEND)

        buildTable(165) {
            Main {
                DRAGON_PLATELEGS quantity 1 rarity 1
                DRAGON_PLATESKIRT quantity 1 rarity 1
                RUNE_FULL_HELM quantity 2 rarity 2
                RUNE_PLATEBODY quantity 2 rarity 2
                RUNE_PLATELEGS quantity 2 rarity 2
                RUNE_KITESHIELD quantity 2 rarity 2
                RUNE_WARHAMMER quantity 2 rarity 2
                DRAGON_LONGSWORD quantity 2 rarity 1
                DRAGON_DAGGER quantity 2 rarity 1
                SUPER_RESTORE4 quantity 4..10 rarity 4
                ONYX_BOLT_TIPS quantity 9..26 rarity 4
                DRAGONSTONE_BOLT_TIPS quantity 60..120 rarity 4
                DRAGONSTONE quantity 8..14 rarity 1
                DEATH_RUNE quantity 90..360 rarity 3
                BLOOD_RUNE quantity 90..360 rarity 3
                LAW_RUNE quantity 100..420 rarity 3
                RUNITE_ORE quantity (5..12).noted rarity 6
                REVENANT_CAVE_TELEPORT quantity (1..3) rarity 6
                ADAMANTITE_BAR quantity (15..24).noted rarity 6
                COAL quantity (100..250).noted rarity 6
                BATTLESTAFF quantity 8.noted rarity 5
                BLACK_DRAGONHIDE quantity (13..24).noted rarity 6
                MAHOGANY_PLANK quantity (19..37).noted rarity 5
                MAGIC_LOGS quantity (20..55).noted rarity 2
                YEW_LOGS quantity (70..180).noted rarity 3
                MANTA_RAY quantity (45..90).noted rarity 3
                RUNITE_BAR quantity (5..12).noted rarity 6
                BLIGHTED_ANCIENT_ICE_SACK quantity (8..55) rarity 9
                BLIGHTED_ENTANGLE_SACK quantity 8..55 rarity 9
                BLIGHTED_TELEPORT_SPELL_SACK quantity 8..55 rarity 9
                BLIGHTED_VENGEANCE_SACK quantity 8..55 rarity 9
                BLIGHTED_SURGE_SACK quantity 8..55 rarity 9
            }
        }
    }
}
