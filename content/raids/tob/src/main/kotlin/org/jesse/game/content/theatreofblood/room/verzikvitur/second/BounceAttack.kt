package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.DirectionUtil
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.masks.*
import org.jesse.game.world.entity.player.MovementLock
import org.jesse.game.world.entity.player.Player

/**
 * @author Jire
 */

internal fun VerzikVitur.bounceAttack() {
    if (Utils.randomBoolean(4)) setForceTalk("There's nothing under there for you!")
    animation = bounceAttackAnimation
    for (p in room.validTargets) {
        if (!p.underVerzik(room)) continue

        p.stopAll()
        p.stun(7)

        WorldTasksManager.schedule {
            p.graphics = stunGraphics

            p.applyHit(Hit(this, Utils.random(50), HitType.REGULAR))

            val dir = DirectionUtil.getDirection(middleLocation, p.location)
            val to = p.location.transform(dir, 2)
            p.animation = forceMoveAnimation
            p.forceMovement = ForceMovement(to, 40, dir.direction)
            WorldTasksManager.schedule({
                p.setLocation(to)
            }, 1)
        }
    }
}

private val bounceAttackAnimation = Animation(8116)
private val stunGraphics = Graphics(80, 0, 96)
private val forceMoveAnimation = Animation(8043)

internal fun Player.underVerzik(room: VerzikViturRoom): Boolean {
    val l = location
    val bl = room.getBaseLocation(30, 24)
    val tr = room.getBaseLocation(34, 28)
    return l.x >= bl.x && l.y >= bl.y && l.x <= tr.x && l.y <= tr.y
}