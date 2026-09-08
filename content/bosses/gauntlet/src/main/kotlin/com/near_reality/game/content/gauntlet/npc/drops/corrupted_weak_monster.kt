package com.near_reality.game.content.gauntlet.npc.drops

import com.near_reality.game.content.gauntlet.gauntletReceivedWeaponFrame
import com.near_reality.game.content.gauntlet.gauntletWeakMonsterKills
import com.near_reality.scripts.npc.drops.table.always
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
import com.zenyte.game.item.ItemId.CORRUPTED_SHARDS
import com.zenyte.game.item.ItemId.CORRUPTED_TELEPORT_CRYSTAL
import com.zenyte.game.item.ItemId.GRYM_LEAF_23875
import com.zenyte.game.item.ItemId.RAW_PADDLEFISH
import com.zenyte.game.item.ItemId.WEAPON_FRAME

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
