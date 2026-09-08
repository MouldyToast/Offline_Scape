package org.jesse.game.content.skills.magic.spells.arceuus

import org.jesse.game.gameClock
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.ProjectileUtils
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.attribute
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.calog.CAType

/**
 * @author Kris | 16/06/2022
 */
private var ThrallNPC.stepIncrement by attribute("thrall_step_increment", 0)
class ThrallNPC(id: Int, location: Location) : NPC(id, location, true) {
    override fun processNPC() {
        super.processNPC()
        interact()
    }

    override fun isEntityClipped(): Boolean {
        return false
    }

    private fun interact() {
        val player = summoner
        if (player == null || player.isFinished) {
            finish()
            return
        }
        val target = player.lastTarget ?: return
        if (target !is NPC || target.isFinished || target.isDead) return
        if (player.attackingDelay + 8000 < Utils.currentTimeMillis()) return
        setFacedInteractableEntity(target)
        val type = ThrallSpell[this]
        val inRange = Utils.isOnRange(x, y, size, target.x, target.y, target.size, type.attackRange)
        if (!inRange || ProjectileUtils.isProjectileClipped(
                this, target, this.location, target.middleLocation, false, true
        )) {
            val stepIncrement = stepIncrement++ % target.size
            val destinationTile = target.location.transform(stepIncrement, stepIncrement)
            addWalkStepsInteract(destinationTile.x, destinationTile.y, 1, size, true)
            return
        }
        resetWalkSteps()
        if (attackClock < gameClock()) {
            randomWalkDelay = Int.MAX_VALUE
            attackClock = gameClock() + 4
            attack(type, target)
        }
    }

    private fun attack(spell: ThrallSpell, target: NPC) {
        val type = spell.type
        setAnimation(Animation(type.attackAnim))
        World.sendSoundEffect(location, SoundEffect(type.attackSound, 12))
        val proj = type.projectile
        val delay = if (proj == null) 0 else World.sendProjectile(this, target, type.projectile)
        val dur = type.projectile?.getProjectileDuration(this, target) ?: 0
        if (type.hitSound != -1) World.sendSoundEffect(target.location, SoundEffect(type.hitSound, 12, dur))
        WorldTasksManager.schedule({
            val hit = Hit(
                summoner,
                Utils.random(spell.maxHit),
                HitType.REGULAR
            )
            val player = summoner
            if (player != null && !player.isFinished) {
                if (player.variables.thrallDamageDone < 100) {
                    player.variables.setThrallDamageDone(player.variables.thrallDamageDone + hit.damage)
                }
                //check separately so players can still complete if this somehow got missed
                if (player.variables.thrallDamageDone >= 100) {
                    player.combatAchievements.complete(CAType.SIT_BACK_AND_RELAX)
                }
            }
            hit.weapon = "Thrall"
            target.applyHit(hit)
            target.autoRetaliate(summoner)
        }, delay)
    }
}