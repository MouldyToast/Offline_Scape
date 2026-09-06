package com.near_reality.game.world.entity.player

import com.zenyte.cores.CoresManager
import org.rsmod.game.events.PlayerDeathStartEvent
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player

fun Player.sendDeath(source: Entity? = null, onDeath: () -> Unit) {
    animation = Animation.STOP
    lock()
    stopAll()
    CoresManager.worldThread?.eventBus?.publish(PlayerDeathStartEvent(this, source))
    WorldTasksManager.schedule(object : WorldTask {
        var ticks = 0
        override fun run() {
            if (isFinished || isNulled) {
                stop()
                return
            }
            when (ticks) {
                0 -> animation = Player.DEATH_ANIMATION
                2 -> {
                    sendMessage("Oh dear, you have died.")
                    onDeath()
                    reset()
                    blockIncomingHits(5)
                    animation = Animation.STOP
                    if (variables.isSkulled)
                        variables.setSkull(false)
                    setLocation(respawnPoint.location)
                }
                3 -> {
                    unlock()
                    appearance.resetRenderAnimation()
                    animation = Animation.STOP
                    stop()
                }
            }
            ticks++
        }
    }, 0, 1)
}
