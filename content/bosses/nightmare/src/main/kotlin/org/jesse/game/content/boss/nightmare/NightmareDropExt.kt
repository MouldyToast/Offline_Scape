package org.jesse.game.content.boss.nightmare

import org.jesse.game.content.follower.impl.BossPet
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.NPCDrops
import org.jesse.game.world.entity.player.Player

object NightmareDropExt {
    @JvmStatic fun dropForPlayer(
        player: Player?,
        npc: BaseNightmareNPC,
        playersOnStart: Int,
        boostActive: Boolean,
        phosanis: Boolean
    ) {
        if(player == null)
             return
        var petRate =
            if (phosanis) BaseNightmareNPC.LITTE_NIGHTMARE_RATE_PHOSANIS else BaseNightmareNPC.rateForPet(playersOnStart)
        if (boostActive) {
            petRate = (petRate * 0.75).toInt()
        }
        BossPet.LITTLE_NIGHTMARE.roll(player, petRate)
        val drops = NPCDrops.getTable(NightmareNPC.AWAKE_P3)
        if (drops != null) {
            NPCDrops.rollTable(player, drops) { drop: Drop ->
                checkTop3AndDrop(npc, player, drop, player.location)
            }
            NPCDrops.rollTable(player, drops) { drop: Drop ->
                checkTop3AndDrop(npc, player, drop, player.location)
            }
        }
    }

    private fun checkTop3AndDrop(npc: BaseNightmareNPC, player: Player, drop: Drop, location: Location) {
        if(npc.getSortedT3DamageMap().contains(player)) {
            npc.dropItem(player, drop, location)
        } else {
            player.putBooleanTemporaryAttribute("nmDropSuppress", true)
            npc.dropItem(player, drop, location)
        }
    }
}

fun BaseNightmareNPC.getSortedT3DamageMap(): MutableList<Player> {
    val damageMap = this.receivedDamage.entries.sortedByDescending { it.value.sumOf { pair -> pair.damage } }
    val playerList = mutableListOf<Player>()
    for (i in 0 until 3) {
        val (pair, _) = damageMap.getOrNull(i) ?: break
        val player = World.getPlayer(pair.first).orElse(null) ?: continue
        playerList.add(player)
    }
    return playerList
}