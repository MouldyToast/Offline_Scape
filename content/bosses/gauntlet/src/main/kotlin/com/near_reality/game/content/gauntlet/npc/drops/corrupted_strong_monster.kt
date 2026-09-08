package com.near_reality.game.content.gauntlet.npc.drops

import com.near_reality.game.content.gauntlet.gauntletReceivedWeaponFrame
import com.near_reality.game.content.gauntlet.gauntletStrongMonsterKills
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ids.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

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
