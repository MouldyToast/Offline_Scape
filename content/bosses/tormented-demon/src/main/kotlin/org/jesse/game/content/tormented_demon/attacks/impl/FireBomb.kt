package org.jesse.game.content.tormented_demon.attacks.impl

import org.jesse.game.content.*
import org.jesse.game.content.tormented_demon.TormentedDemon
import org.jesse.game.content.tormented_demon.attacks.Attack
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.AbstractEntity
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-16
 */
class FireBomb : Attack {

    /*
     * briefly bind the player in place roughly every 60 ticks and disable their run,
     * then release two firebombs;
     * one on the player's current position when the attack was used,
     * and one in a 3x3 AoE from the first shot.
     *
     * The demon will change its combat style after this attack.
     */

    private val fireBombProjectile : Projectile =
        Projectile(2855, 64, 32, 96, 0)

    override fun invoke(demon: TormentedDemon, target: Entity?) {
        if (target == null) return
        target.isRun = false
        // Animate the attack
        demon seq demon.getFireSkullThrowAnimation()
        // grab the locations of the bombs
        val targetLocation = target.location.copy()
        val secondBombLocation = getSecondBombLocation(targetLocation)
        // throw the bombs
        World.sendProjectile(demon.location, targetLocation, fireBombProjectile)
        World.sendProjectile(demon.location, secondBombLocation, fireBombProjectile)
        // Schedule the hits
        schedule(4) {
            if (target.location == targetLocation)
                target.scheduleHit(demon, demon hit target damage Random.nextInt(40, 45), 0)
        }
        schedule(5) {
            if (target.location == secondBombLocation)
                target.scheduleHit(demon, demon hit target damage Random.nextInt(40, 45), 0)
        }
        // reset the Player Accuracy Boost
        demon.accuracyBoostTimer.reset()
    }

    private fun getSecondBombLocation(location: Location?): Location {
        val xOffset = Random.nextInt(-1, 1)
        val yOffset = Random.nextInt(-1, 1)
        val tempLocation = Location(location?.offset(Pair(xOffset, yOffset)))
        if (tempLocation == location)
            return getSecondBombLocation(location)
        return tempLocation
    }
}