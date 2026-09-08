package org.jesse.game.content.theatreofblood.room.maidenofsugadinti.npc

import org.jesse.game.content.theatreofblood.room.TheatreNPC
import org.jesse.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiRoom
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.npc.ids.*

/**
 * @author Tommeh
 * @author Jire
 */
internal class NylocasMatomenos(private val maiden: MaidenOfSugadinti, tile: Location) :
    TheatreNPC<MaidenOfSugadintiRoom>(
        maiden.room,
        NYLOCAS_MATOMENOS,
        maiden.room.getLocation(tile)
    ) {

    // TODO freeze chances based on magic bonus

    private var dying = false

    init {
        setFaceEntity(maiden)
        isForceMultiArea = true
    }

    override fun processNPC() {
        if (dying || isFinished || isDead || room.completed) return
        if (!hasWalkSteps() && !isFrozen && Utils.getDistance(x, y, maiden.middleLocation.x, maiden.middleLocation.y) < 6) {
            if (!dying) absorb()
            return
        }
        if (!isFrozen && !hasWalkSteps())
            addWalkSteps(maiden.location.x, maiden.location.y)
    }

    override fun sendDeath() {
        dying = true
        setAnimation(Animation(8097))
        WorldTasksManager.schedule({
            onFinish(null)
        }, 1)
    }

    private fun absorb() {
        maiden.absorbNylocas(this)
        setHitpoints(0)
    }

    override fun autoRetaliate(source: Entity) {}

    override fun isDying() = dying

}