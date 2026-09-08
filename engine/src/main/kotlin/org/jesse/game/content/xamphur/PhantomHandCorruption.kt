package org.jesse.game.content.xamphur

import org.jesse.game.task.TickTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.attribute
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.varbit

private var Player.corruptionCycle by varbit(12141)
private var Player.corruptionOrbEffect by varbit(12142)
var Entity.hasMarkOfDarkness by attribute("mark_of_darkness_effect", false)
var Player.hasCorruption by attribute("has_corruption", false)

fun applyCorruptionEffect(target: Player) {
    if (target.hasCorruption)
        return
    val probability = 66
    if (Utils.random(100) >= probability) return
    if(target.prayerManager.prayerPoints == 0)
        return
    target.sendMessage("<col=ef0083>You have been corrupted!</col>")
    target.corruptionCycle = 1
    target.hasCorruption = true;
    target.corruptionOrbEffect = 3
    target.packetDispatcher.sendClientScript(82, 10485773, 10485776, 10485778, 10485777, 10485780, 10485774, 10485775)
    target.scheduleCorruptionEffect(if (target.hasMarkOfDarkness) 5 else 10)
}

fun Player.scheduleCorruptionEffect(interval: Int) = WorldTasksManager.schedule(object : TickTask() {
    override fun run() {
        if(isDying) {
            stop()
            return
        }
        val nextCycle = ++corruptionCycle
        val drainAmount = nextCycle.dec()
        if(prayerManager.prayerPoints == 0)
            return
        prayerManager.drainPrayerPoints(drainAmount)
        applyHit(Hit(drainAmount, HitType.CORRUPTION))
        if (nextCycle == 4) {
            sendMessage("<col=ef0083>You are no longer afflicted with corruption.</col>")
            stop()
        }
    }

    override fun stop() {
        hasCorruption = false;
        corruptionCycle = 0
        corruptionOrbEffect = 0
        packetDispatcher.sendClientScript(82, 10485773, 10485776, 10485778, 10485777, 10485780, 10485774, 10485775)
        super.stop()
    }
}, interval.dec(), interval)