package com.near_reality.game.content.araxxor

import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.araxxor.attacks.impl.CleaveAttack
import com.near_reality.game.content.araxxor.attacks.impl.MeleeAttack
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Entity
import kotlin.math.absoluteValue

fun Attack.isMelee() = this is MeleeAttack || this is CleaveAttack

fun Araxxor.inMeleeRange() = instance?.player?.let { isWithinMeleeDistance(this, it) } ?: false

fun Araxxor.debug(msg: String) = instance?.player?.sendDeveloperMessage(msg)

fun Araxxor.rangedCombatMode(target: Entity) {
    debug("Araxxor: Starting ranged combat strategy.")
    isForceFollowClose = false
    activeAttackStyle = getDistanceAttack(target)
}

fun Araxxor.meleeCombatMode() {
    debug("Araxxor: Enforcing next melee attack. Pathing to target.")
    isForceFollowClose = true
    activeAttackStyle = getMeleeAttack()
}

fun Araxxor.enrageCombatMode() {
    debug("Araxxor: Enraged. Starting Cleave Attack. Pathing to target.")
    isForceFollowClose = true
    activeAttackStyle = CleaveAttack()
}

/**
 * We only care about the 2D angle of where these pools are going
 *      If this returns SOUTH, we spawn along x axis
 *      If this returns EAST, we spawn along y axis
 *
 * X delta will be higher than Y delta if player is on east or west side
 * Y delta will be higher than X delta if player is on north or south side
 *
 * If player is on a corner, select a pattern at random
 */
fun Araxxor.determinePlayerAxis() : Direction {
    val player = instance?.player ?: return Direction.NORTH
    val xDelta = (player.position.x - this.middleLocation.x).absoluteValue
    val yDelta = (player.position.y - this.middleLocation.y).absoluteValue

    if(xDelta > yDelta) return Direction.EAST
    if(yDelta > xDelta) return Direction.SOUTH

    return directionList.random()
}

val directionList = mutableListOf(Direction.EAST, Direction.SOUTH)
