package org.jesse.game.content.gauntlet.npc.drops

import org.jesse.game.content.gauntlet.gauntletReceivedWeaponFrame
import org.jesse.game.content.gauntlet.gauntletStrongMonsterKills
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

class CorruptedStrongMonsterDroptable : NPCDropTableScript() {

    val common = 100

    val uncommon = 50

    init {
        npcs(
            CORRUPTED_UNICORN,
            CORRUPTED_SCORPION,
            CORRUPTED_WOLF
        )


        buildTable(250) {
            Always {
                CORRUPTED_SHARDS quantity 50..105 rarity always
            }
            Main {
                WEAPON_FRAME quantity 1 dynamicRarity {
                    if (!gauntletReceivedWeaponFrame && gauntletStrongMonsterKills == 1) always else common
                } transformItem { item ->
                    gauntletReceivedWeaponFrame = true
                    item
                } info {
                    "Guaranteed weapon frame drop if you haven't received one before."
                }
                RAW_PADDLEFISH quantity 4 rarity common
                CORRUPTED_TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }
        }
    }
}
