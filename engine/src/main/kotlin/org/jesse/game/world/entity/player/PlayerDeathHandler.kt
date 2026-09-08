package org.jesse.game.world.entity.player

import org.jesse.game.content.skills.magic.spells.arceuus.invokeDeathChargeEffect
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player

fun Player.sendDeath(source: Entity? = null, onDeath: () -> Unit) {
    animation = Animation.STOP
    lock()
    stopAll()
    if (prayerManager.isActive(Prayer.RETRIBUTION))
        prayerManager.applyRetributionEffect(source)
    if (source is Player)
        source.invokeDeathChargeEffect()
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
