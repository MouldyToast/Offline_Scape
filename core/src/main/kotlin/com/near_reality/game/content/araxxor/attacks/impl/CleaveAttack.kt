package com.near_reality.game.content.araxxor.attacks.impl

import com.zenyte.game.world.entity.player.PrayerVarbits
import com.near_reality.game.content.araxxor.Araxxor
import com.near_reality.game.content.araxxor.debug
import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.araxxor.determinePlayerAxis
import com.near_reality.game.content.hit
import com.near_reality.game.content.offset
import com.near_reality.game.content.seq
import com.near_reality.game.content.withVenom
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import com.zenyte.logger.NearRealityLogger
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
            if (target.varManager.getBitValue(PrayerVarbits.PROTECT_FROM_MELEE) == 1)
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