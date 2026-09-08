package org.jesse.game.content.dt2.npc.leviathan

import org.jesse.game.content.dt2.npc.*
import org.jesse.game.content.dt2.npc.leviathan.Leviathan
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants
import org.jesse.game.content.dt2.npc.leviathan.LeviathanFightArea
import org.jesse.game.content.dt2.npc.leviathan.LeviathanInstance
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import java.lang.ref.WeakReference

class LeviathanTornado(
    private val targeReference: WeakReference<Player>,
    private val leviathan: WeakReference<Leviathan>,
    position: Location,
    private val instance: LeviathanInstance
) : NPC(
    12222, position, Direction.NORTH, 0
) {

    override fun processNPC() {
        val target = targeReference.get() ?: return remove()
        walkToTarget(target)

        super.processNPC()
    }

    private fun walkToTarget(target: Player) {
        if (isFinished) return resetWalkSteps()

        val targetPosition = walkToCardinal(target.location, ignorePathfinding = true)
        if (!targetPosition.inArea(LeviathanFightArea::class, instance)) {
            return resetWalkSteps()
        }

        if (position != target.position) {
            return
        }

        target.applyHit(
            Hit(
                random(40..50), HitType.DEFAULT
            )
        )
        location.playSound(LeviathanConstants.LEVIATHAN_TORNADO_HIDDEN_SOUND.id, delay = 0, radius = 6)
        target.graphics = LeviathanConstants.LEVIATHAN_TORNADO_HIT

        remove()
        resetWalkSteps()

        val location = location.copy()

        val leviathan = leviathan.get() ?: return remove()
        leviathan.schedule(delay = 8) {
            setRespawnTile(ImmutableLocation(location))
            spawn()
        }
    }
}