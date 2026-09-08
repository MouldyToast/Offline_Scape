package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.theatreofblood.awardMostDamageContributionPointsToMVPForPhase
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur.Companion.TRANSFORM_INTO_SECOND_PHASE_ID
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturPhase
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import org.jesse.game.item.ids.*
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * @author Jire
 */

internal fun VerzikVitur.secondPhase() {
    ticks++
    if (ticks <= 0 || ticks % 4 != 0) return
    if (shielded()) return

    if (!healPhase && hitpointsAsPercentage <= 35) {
        healPhase = true
        attackCounter = 0
        spawnMatomenos()
        return
    }

    var canBounce = false
    for (p in room.validTargets) {
        if (p.underVerzik(room)) {
            canBounce = true
        }
    }

    if (canBounce) {
        bounceAttack()
    } else {
        if (!healPhase) {
            if (attackCounter == 4) {
                attackCounter = 0
                electricAttackCounter++
                electricAttack()
            } else if (attackCounter == -1 || electricAttackCounter == 5) {
                animation = lampAttackAnimation
                electricAttackCounter = 0
                spawnPurpleCrab()
                spawnCrabs()
                if (attackCounter == -1) {
                    attackCounter = 0
                }
            } else {
                attackCounter++
                lampAttack()
            }
        } else {
            if (!room.raid.bypassMode && attackCounter > 0 && attackCounter % 7 == 0) {
                spawnMatomenos()
            } else {
                val random = Utils.random(100)
                if (random >= 25) {
                    bloodAttack()
                } else {
                    lampAttack()
                }
            }
            attackCounter++
        }
    }
}

internal fun VerzikVitur.switchToSecondPhase() {

    room.awardMostDamageContributionPointsToMVPForPhase()

    for (p in room.entered) {
        p.varManager.sendBit(VerzikViturRoom.THRONE_VARBIT, 1)

        val amount = p.inventory.getAmountOf(DAWNBRINGER)
        if (amount > 0) {
            p.inventory.deleteItem(DAWNBRINGER, amount)
        }
        if (p.equipment.getId(EquipmentSlot.WEAPON) == DAWNBRINGER) {
            p.equipment.set(EquipmentSlot.WEAPON, null)
            p.equipment.container.refresh()
            p.combatDefinitions.autocastSpell = null
            p.combatDefinitions.refresh()
            p.appearance.resetRenderAnimation()
            p.sendMessage("The weapon falls apart in your hand as Verzik's shield is destroyed.")
        }
    }

    //properties.isRetaliating = false
    isCantInteract = true
    animation = hopOffThroneAnim
    lock()
    temporaryAttributes["ignoreWalkingRestrictions"] = true
    phase = VerzikViturPhase.NONE

    WorldTasksManager.schedule({
        animation = Animation(8112)
        setTransformation(TRANSFORM_INTO_SECOND_PHASE_ID)
        heal(maxHitpoints)
        room.refreshHealthBar()

        for (i in 0..room.supportingPillars.lastIndex) {
            val pillar = room.supportingPillars[i] ?: continue
            if (!pillar.isFinished) {
                pillar.sendDeath()
            }
            room.supportingPillars[i] = null
        }

        WorldTasksManager.schedule({
            animation = Animation.STOP
            val dest = room.getBaseLocation(30, 24)

            addWalkSteps(dest.x, dest.y, -1, false)

            WorldTasksManager.schedule({
                ticks = 0
                setLocation(room.getBaseLocation(31, 25))
                switchPhase(VerzikViturPhase.SECOND)
                faceDirection(Direction.SOUTH)
                isCantInteract = false
                temporaryAttributes.remove("ignoreWalkingRestrictions")
                unlock()
            }, 13)
        }, 1)
    }, 3)
}

private val hopOffThroneAnim = Animation(8111)
