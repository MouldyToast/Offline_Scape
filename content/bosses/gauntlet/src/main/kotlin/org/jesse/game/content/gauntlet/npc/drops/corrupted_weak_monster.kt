package org.jesse.game.content.gauntlet.npc.drops

import org.jesse.game.content.gauntlet.gauntletReceivedWeaponFrame
import org.jesse.game.content.gauntlet.gauntletWeakMonsterKills
import org.jesse.scripts.npc.drops.table.always
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

class CorruptedWeakMonsterDroptable : NPCDropTableScript() {

    val common = 50

    val uncommon = 15

    init {
        npcs(
            CORRUPTED_RAT,
            CORRUPTED_SPIDER,
            CORRUPTED_BAT
        )


        buildTable(165) {
            Always {
                CORRUPTED_SHARDS quantity 10..37 rarity always
            }
            Main {
                WEAPON_FRAME quantity 1 dynamicRarity {
                    if (!gauntletReceivedWeaponFrame && gauntletWeakMonsterKills == 3) always else common
                } transformItem { item ->
                    gauntletReceivedWeaponFrame = true
                    item
                } info {
                    "The third killed weak monster has a guaranteed weapon frame drop if you haven't received one before."
                }
                RAW_PADDLEFISH quantity 1..3 rarity common
                GRYM_LEAF_23875 quantity 1..3 rarity common
                CORRUPTED_TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }
        }
    }
}
