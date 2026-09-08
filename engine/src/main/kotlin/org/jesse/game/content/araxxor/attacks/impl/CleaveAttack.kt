package org.jesse.game.content.araxxor.attacks.impl

import org.jesse.game.content.araxxor.Araxxor
import org.jesse.game.content.araxxor.debug
import org.jesse.game.content.araxxor.attacks.Attack
import org.jesse.game.content.araxxor.determinePlayerAxis
import org.jesse.game.content.hit
import org.jesse.game.content.offset
import org.jesse.game.content.seq
import org.jesse.game.content.withVenom
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities
import org.jesse.logger.NearRealityLogger
import org.slf4j.Logger

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class CleaveAttack: Attack {

    val logger: Logger = NearRealityLogger.getLogger(this::class.java)

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null) return
        araxxor.forceTalk = ForceTalk("Skree!")
        araxxor seq 11483
        val centerTile = target.location.copy()
        schedule(1) { stomp(araxxor, target, centerTile) }
        araxxor.isForceFollowClose = false
    }

    private fun stomp(araxxor: Araxxor, target: Entity, centerTile: Location) {
        if(araxxor.debugCleaveAttack) araxxor.debug("Araxxor: Initial stomp task started")
        if (target is Player && target.mapInstance != null) {
            if(araxxor.debugCleaveAttack) araxxor.debug("Araxxor: Calculating stomp, initial checks are success")
            var maxHit = 38
            if (target.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
                maxHit = (maxHit * 0.20).toInt()
            val damage = CombatUtilities.getRandomMaxHit(araxxor, maxHit, AttackType.CRUSH, target)
            if (target.location == centerTile) {
                target.scheduleHit(araxxor, araxxor hit target withVenom damage, 0)
                if(araxxor.debugCleaveAttack) araxxor.debug("Araxxor: Calculating stomp at location $centerTile")
            }
            // Add acid pools on the player's location, and on either side of the player
            araxxor.instance?.spawnAcidPool(centerTile)
            val axis = araxxor.determinePlayerAxis()

            if (axis == Direction.SOUTH) {
                araxxor.instance?.spawnAcidPool(centerTile offset Pair(-1, 0))
                araxxor.instance?.spawnAcidPool(centerTile offset Pair(1, 0))
            }
            if (axis == Direction.EAST) {
                araxxor.instance?.spawnAcidPool(centerTile offset Pair(0, -1))
                araxxor.instance?.spawnAcidPool(centerTile offset Pair(0, 1))
            }
        }
    }
}