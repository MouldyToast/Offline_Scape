package org.jesse.game.content.theatreofblood.room.verzikvitur.spiders

import org.jesse.game.content.theatreofblood.room.TheatreNPC
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.npc.combat.CombatScript
import org.jesse.game.world.entity.player.Player

/**
 * @author Jire
 */
internal abstract class NylocasChaseCrab(room: VerzikViturRoom, id: Int, location: Location) :
    TheatreNPC<VerzikViturRoom>(room, id, location, Direction.SOUTH), CombatScript {

    var playerToChase: Player? = null
    abstract val styleToAttack: HitType

    override fun attack(target: Entity?) = 0

    override fun applyHit(hit: Hit) {
        super<TheatreNPC>.applyHit(hit)

        if (hit.hitType == styleToAttack) return
        val source = hit.source as? Player ?: return
        hit.damage = 0
        source.sendMessage("You can only damage the ${definitions.name} with ${styleToAttack.name.lowercase()}.")
    }

    override fun spawn(): NPC = super.spawn().apply {
        playerToChase = Utils.random(room.validTargets) ?: return@apply

        WorldTasksManager.schedule({
            if (room.verzikVitur.isFinished || room.verzikVitur.isDead || room.completed) {
                finish()
                return@schedule
            }

            if (isDead || isFinished) {
                return@schedule
            }

            death()
        }, 25)
    }

    override fun processNPC() {
        super.processNPC()

        if (room.verzikVitur.isFinished || room.verzikVitur.isDead || room.completed) {
            finish()
            return
        }

        if (isDead || isFinished) {
            return
        }

        val player = playerToChase ?: return
        resetWalkSteps()
        calcFollow(player, -1, true, false, false)
        setFaceEntity(player)
        if (middleLocation.withinDistance(player.location, 2) && room.isValidTarget(player)) {
            death()
        }
    }

    private fun death() {
        playerToChase = null
        lock()
        resetWalkSteps()
        setFaceEntity(null)
        WorldTasksManager.schedule {
            val spawnDefinitions = combatDefinitions.spawnDefinitions
            setAnimation(spawnDefinitions.deathAnimation)
            hitpoints = 0
            WorldTasksManager.schedule {
                onFinish(null)
            }
        }
    }

    override fun autoRetaliate(source: Entity?) {
    }

    override fun sendDeath() {

    }

    override fun checkAggressivity(): Boolean {
        return false
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        for (p in room.validTargets) {
            val dist = p.location.getTileDistance(middleLocation)
            if (dist < 4) {
                val damage = distanceDamage[0.coerceAtLeast(dist - 1)]
                if (damage > 0) {
                    p.applyHit(Hit(this, damage, HitType.REGULAR))
                }
            }
        }
    }

    override fun isValidAnimation(animID: Int) = true

    companion object {
        val distanceDamage = arrayOf(63, 26, 8, 0)
    }

}