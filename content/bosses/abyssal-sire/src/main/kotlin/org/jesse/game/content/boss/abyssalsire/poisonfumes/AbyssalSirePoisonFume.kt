package org.jesse.game.content.boss.abyssalsire.poisonfumes

import org.jesse.game.content.boss.abyssalsire.AbyssalNexusArea
import org.jesse.game.content.boss.abyssalsire.AbyssalSire
import org.jesse.game.util.Utils
import org.jesse.game.world.WorldThread
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSirePoisonFume(
    val location: Location,
    private var lifespan: Int = POISON_FUME_LIFESPAN_TICKS,
    private var processTick: Long = WorldThread.getCurrentCycle() + 2
) {

    fun remove(sire: AbyssalSire): Boolean {
        val area = sire.lair
        val needsRemoving = --lifespan <= 0
        if (WorldThread.getCurrentCycle() >= processTick) {
            for (player in area.players) {
                if (player.isNulled || player.isFinished || player.isTeleported || player.isDead) continue
                val distance = location.getTileDistance(player.location)
                if (distance > 1) continue
                val damage = if (distance == 0) Utils.random(10, 30) else Utils.random(2, 8)
                player.applyHit(Hit(damage, HitType.POISON))
                sire.hitByPool = true
                sire.perfectSire = false
            }
        }
        return needsRemoving
    }

    private companion object {
        const val POISON_FUME_LIFESPAN_TICKS = 5
    }

}
