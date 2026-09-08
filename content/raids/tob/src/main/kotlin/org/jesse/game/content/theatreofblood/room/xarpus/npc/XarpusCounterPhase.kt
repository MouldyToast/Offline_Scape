package org.jesse.game.content.theatreofblood.room.xarpus.npc

import org.jesse.game.world.World
import org.jesse.game.world.WorldThread
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player
import org.jesse.utils.TimeUnit

/**
 * @author Jire
 * @author Tommeh
 */
internal class XarpusCounterPhase(xarpus: Xarpus) : XarpusPhase(xarpus) {

    private var currentQuadrant = XarpusQuadrant.random()
    private var currentTurnStart = 0L
    private var safePeriod = 0L

    override fun onPhaseStart() {
        xarpus.forceTalk = screech
        safePeriod = WorldThread.getCurrentCycle() + turnIncrement
        ticks = 0
    }

    override fun onTick() {
        xarpus.setFaceEntity(null)
        if (ticks > 0 && ticks % turnIncrement == 0)
            turn()
    }

    private fun turn() {
        currentQuadrant = currentQuadrant.randomOther()
        xarpus.faceLocation = xarpus.room.getLocation(currentQuadrant.cornerTile)
        currentTurnStart = WorldThread.getCurrentCycle()
    }

    fun counter(receivedHit: Hit) {
        if (safePeriod >= WorldThread.getCurrentCycle()) {
            return
        }

        if (receivedHit.scheduleTime <= currentTurnStart) return
        if (receivedHit.weapon is String && (receivedHit.weapon as String).equals("thrall", true)) return
        
        val player = receivedHit.source as? Player ?: return
        if (!xarpus.room.isValidTarget(player)) return

        if (!currentQuadrant.isInside(player.location, xarpus)) return

        player.applyHit(
            Hit(player.hitpoints.coerceAtMost(BASE_DAMAGE + receivedHit.damage / 2), HitType.POISON)
        )
    }

    override val isPhaseComplete get() = xarpus.isDying || xarpus.isDead

    override fun advance(): XarpusPhase? {
        for (poisonSplat in xarpus.poisonSplats)
            World.removeObject(poisonSplat)
        xarpus.poisonSplats.clear()
        return null
    }

    private companion object {

        const val BASE_DAMAGE = 50

        val turnIncrement = TimeUnit.SECONDS.toTicks(5).toInt()

        val screech = ForceTalk("Screeeeech!")

    }

}
