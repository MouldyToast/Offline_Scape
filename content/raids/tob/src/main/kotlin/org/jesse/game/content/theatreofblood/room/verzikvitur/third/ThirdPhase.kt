package org.jesse.game.content.theatreofblood.room.verzikvitur.third

import org.jesse.game.content.theatreofblood.awardMostDamageContributionPointsToMVPForPhase
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturPhase
import org.jesse.game.content.theatreofblood.room.verzikvitur.third.passivespells.DotPassiveSpell
import org.jesse.game.content.theatreofblood.room.verzikvitur.third.passivespells.NylocasPassiveSpell
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.CollisionUtil
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.masks.Animation

/**
 * @author Jire
 */

internal fun VerzikVitur.thirdPhase(victim: Entity) {
    if (attackCounter == 4) {
        previousTarget = combat.target
        passiveSpell = passiveSpell.nextSpell
        passiveSpell.run { cast() }
        attackCounter = 0
        if (passiveSpell != NylocasPassiveSpell) {
            return
        }
    }

    if (middleLocation.getTileDistance(victim.location) == 4 && !CollisionUtil.collides(x, y, size, victim.x, victim.y, victim.size) && Utils.randomBoolean()) {
        meleeAttack(victim)
    } else if (Utils.randomBoolean()) {
        magicAttack()
    } else {
        rangeAttack()
    }
    attackCounter++
}

internal fun VerzikVitur.processThirdPhase() {
    if (!spawnedTornados && hitpointsAsPercentage <= 20) {
        spawnedTornados = true
        spawnTornados()
    }
}

internal fun VerzikVitur.switchToThirdPhase() {
    if (crabs.isNotEmpty()) {
        for (crab in crabs) {
            crab.finish()
        }
        crabs.clear()
    }

    room.awardMostDamageContributionPointsToMVPForPhase()

    lock()
    resetWalkSteps()
    animation = transformAnimation
    phase = VerzikViturPhase.NONE
    attackCounter = 0

    WorldTasksManager.schedule({
        setTransformation(8373)
        animation = transformAnimation2

        WorldTasksManager.schedule({
            switchPhase(VerzikViturPhase.THIRD)
            animation = Animation.STOP

            setForceTalk("Behold my true nature!")

            setLocation(room.getBaseLocation(29, 23))
            setTarget(Utils.random(room.validTargets))
            unlock()
        }, 2)
    }, 1)
}

private val transformAnimation = Animation(8118)
private val transformAnimation2 = Animation(8119)
