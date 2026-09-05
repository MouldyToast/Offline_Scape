package com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.near_reality.game.content.dt2.npc.*
import com.near_reality.game.content.dt2.npc.vardorvis.VardorvisFightArea
import com.zenyte.game.util.Direction
import com.zenyte.game.util.DirectionUtil
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import mgi.utilities.CollectionUtils

/**
 * @author John J. Woloszyk / Kryeus
 * @date 5.9.2024
 */
class SwingingAxe(
    private val instance: VardorvisInstance,
    val axeLoc: VardorvisInstance.AxeLocationInformation,
    private val static: Boolean = true
) : NPC(
    if (static) 12225
    else 12227,
    if (static) instance.getLocation(axeLoc.headLocation)
    else instance.getLocation(axeLoc.axeLocation),
    axeLoc.direction,
    0,
    true
) {

    init {
        temporaryAttributes["ignoreWalkingRestrictions"] = true
    }

    fun orient() {
        val destination = axeLoc.destination
        val npcCenter = middleLocation
        val angle = getRoundedDirection(
            DirectionUtil.getFaceDirection(
                (npcCenter.x - destination.x).toDouble(),
                (npcCenter.y - destination.y).toDouble()
            ), 1024
        )
        val direction = CollectionUtils.findMatching(Direction.values) { it.npcDirection == angle }!!
        faceLocation = npcCenter.transform(direction)

    }

    fun path(first: Boolean) {
        val direction = axeLoc.direction
        playAnimation(10366)

        val steps = if (first) 1 else 2
        exactMove(
            secondLocation = position.transform(direction, steps),
            secondDuration = 30,
            direction = direction
        )
    }

    fun check(player: Player, first: Boolean): Boolean {
        val direction = axeLoc.direction
        val steps = if (first) 1 else 2
        repeat(steps) {
            val area = OccupiedArea(
                position.transform(direction, -it),
                size
            )
            if (player.occupiedArea.intersectsWith(area)) {
                return true
            }
        }
        return false
    }

    override fun processNPC() {
        super.processNPC()
        if (location == axeLoc.destination || !location.inArea(VardorvisFightArea::class, instance)) {
            remove()
        }
    }

    fun transform(): SwingingAxe {
        this.sendDeath()
        return SwingingAxe(instance, axeLoc, static = false)
    }

    override fun setRespawnTask() {}
}