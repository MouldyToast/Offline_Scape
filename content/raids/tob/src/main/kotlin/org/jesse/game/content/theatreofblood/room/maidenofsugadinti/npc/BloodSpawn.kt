package org.jesse.game.content.theatreofblood.room.maidenofsugadinti.npc

import org.jesse.game.content.theatreofblood.room.TheatreNPC
import org.jesse.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiRoom
import org.jesse.game.content.theatreofblood.room.maidenofsugadinti.`object`.BloodTrail
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.npc.ids.*

/**
 * @author Tommeh
 * @author Jire
 */
internal class BloodSpawn(
    private val maiden: MaidenOfSugadinti,
    private val spawnLocation: Location
) : TheatreNPC<MaidenOfSugadintiRoom>(maiden.room, BLOOD_SPAWN, spawnLocation) {

    init {
        radius = 10
    }

    override fun applyHit(hit: Hit) {
        super.applyHit(hit)

        if (room.raid.hardMode) {
            hit.damage = 0
        }
    }

    override fun processNPC() {
        if (maiden.dead()) return

        val lastTile = if (lastLocation == null) location else lastLocation
        addTrail(lastTile)
        if (shouldNotWalk())
            addTrail(location)

        // custom walk behaviour
        if (!isFrozen && Utils.random(2) == 0) {
            val moveX = Utils.random(-radius, radius)
            val moveY = Utils.random(-radius, radius)
            val respawnX = spawnLocation.x
            val respawnY = spawnLocation.y
            addWalkStepsInteract(respawnX + moveX, respawnY + moveY, radius, getSize(), true)
        }
    }

    private fun addTrail(tile: Location) {
        if (!maiden.splatExists(tile)) {
            val loc = Location(tile)
            if (maiden.addSplat(loc)) {
                val t = BloodTrail(maiden, loc)
                maiden.bloodTrails.add(t)
                t.process()
            }
        } else if (!shouldNotWalk()) {
            // resets timer for existing splat
            for (trail in maiden.bloodTrails) {
                if (trail.positionHash == tile.positionHash) {
                    trail.resetTimer()
                    break
                }
            }
        }
    }

    private fun shouldNotWalk() = isFrozen || isStunned

    override fun setStats() {}

    override fun autoRetaliate(source: Entity) {}

}