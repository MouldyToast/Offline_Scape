package com.near_reality.game.content.skills.hunter.herbiboar

import com.near_reality.game.content.skills.hunter.herbiboar.Herbiboar.currentHerbiboarPath
import com.near_reality.game.content.skills.hunter.herbiboar.Herbiboar.resetHerbiboarVars
import com.near_reality.game.content.skills.hunter.herbiboar.Herbiboar.unharvestedHerbiboar
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Andys1814
 * @since 1/26/2025
 */
@Suppress("unused")
class HerbiboarTunnelObjectAction : ObjectAction {

    companion object {

        private val KICK_ANIMATION = Animation(423)

        private val HERBIBOAR_SPAWN_ANIMATION = Animation(7687)

    }

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val searchingTunnel = HerbiboarTunnel.forLocation(`object`.location) ?: return

        when (option) {
            "Attack" -> {
                player.animation = KICK_ANIMATION

                val finishVarbit = player.varManager.getBitValue(Herbiboar.HB_FINISH)
                if (
                    finishVarbit == 0 || player.currentHerbiboarPath == null ||
                    player.currentHerbiboarPath!!.getCurrentTrail() != null ||
                    player.currentHerbiboarPath!!.tunnel != searchingTunnel) {
                    player.sendMessage("You kick the mound of earth to no effect and look rather silly.")
                    return
                }

                val tunnel = player.currentHerbiboarPath!!.tunnel
                val herbiboar = NPC(NpcId.HERBIBOAR, tunnel.herbiboarSpawnLocation, true)
                herbiboar.radius = 0
                herbiboar.spawn()
                player.sendMessage("Your herbiboar index is ${herbiboar.index}")

                herbiboar.animation = HERBIBOAR_SPAWN_ANIMATION
                player.sendSound(SoundEffect(1106))
                player.sendSound(SoundEffect(1644))

                player.unharvestedHerbiboar = herbiboar.index
                player.currentHerbiboarPath = null
                player.resetHerbiboarVars()
            }
            "Search" -> {

            }
        }
    }

    override fun getObjects() = arrayOf(ObjectId.TUNNEL_30532)

}