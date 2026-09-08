package com.near_reality.game.content.gauntlet.npc.drops

import com.near_reality.game.content.gauntlet.gauntletReceivedBowString
import com.near_reality.game.content.gauntlet.gauntletReceivedOrb
import com.near_reality.game.content.gauntlet.gauntletReceivedSpike
import com.near_reality.scripts.npc.drops.table.always
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import kotlin.reflect.KMutableProperty1
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ids.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class CrystallineDemiBossDroptable : NPCDropTableScript() {

    val common = 48

    val uncommon = 80

    fun tryUpgrade(receivedUpgrade: KMutableProperty1<Player, Boolean>): Player.(Item) -> Item? = { item ->
        if (!receivedUpgrade(this)) {
            receivedUpgrade.set(this, true)
            item
        } else
            buildMap {
                if (!gauntletReceivedOrb) put(CRYSTAL_ORB, Player::gauntletReceivedOrb)
                if (!gauntletReceivedSpike) put(CRYSTAL_SPIKE, Player::gauntletReceivedSpike)
                if (!gauntletReceivedBowString) put(CRYSTALLINE_BOWSTRING, Player::gauntletReceivedBowString)
            }.let {
                if (it.isEmpty()) {
                    null
                } else {
                    val itemId = it.keys.random()
                    val property = it[itemId]!!
                    property.set(this, true)
                    Item(itemId, 1)
                }
            }
    }

    init {
        npcs(
            CRYSTALLINE_BEAR,
            CRYSTALLINE_DRAGON,
            CRYSTALLINE_DARK_BEAST
        )


        buildTable(128) {
            Always {
                CRYSTAL_SHARDS quantity 50..105 rarity always
                WEAPON_FRAME_23871 quantity 1 rarity always
            }
            Main {
                RAW_PADDLEFISH quantity 5 rarity common
                TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }
            Unique {
                CRYSTAL_ORB quantity 1 rarity always onlyDroppedBy CRYSTALLINE_DRAGON transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedOrb)(this, item)
                }
                CRYSTAL_SPIKE quantity 1 rarity always onlyDroppedBy CRYSTALLINE_BEAR transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedSpike)(this, item)
                }
                CRYSTALLINE_BOWSTRING quantity 1 rarity always onlyDroppedBy CRYSTALLINE_DARK_BEAST transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedBowString)(this, item)
                }
            }
        }
    }
}
