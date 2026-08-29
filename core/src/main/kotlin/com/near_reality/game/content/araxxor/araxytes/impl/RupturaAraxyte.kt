package com.near_reality.game.content.araxxor.araxytes.impl

import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.offset
import com.near_reality.game.content.seq
import com.near_reality.game.content.spotanim
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.RUPTURA_ARAXYTE
import com.zenyte.game.world.entity.npc.NpcId.RUPTURA_ARAXYTE_EGG

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class RupturaAraxyte(
    val instance: AraxxorInstance,
    spawnLocation: Location,
    private var exploding: Boolean = false
): Araxyte(
    RUPTURA_ARAXYTE_EGG,
    spawnLocation
) {

    override fun attack(target: Entity?): Int {
        if (getId() == RUPTURA_ARAXYTE_EGG) return 1
        if (target == null) return 1
        if (exploding) return 1

        if (isInRangeOfTarget(target)) {
            exploding = true
            lock()
            this seq 11510
            schedule(2) {
                explode(target)
                remove()
            }
        }
        return 1
    }

    private fun NPC.isInRangeOfTarget(target: Entity): Boolean {
        // Check the SW tile
        return target.location.withinDistance(this, 1) ||
                // Check the SE tile
                target.location.withinDistance(location offset Pair(1, 0), 1) ||
                // Check the NW tile
                target.location.withinDistance(location offset Pair(0, 1), 1) ||
                // Check the NE tile
                target.location.withinDistance(location offset Pair(1, 1), 1)
    }

    private fun explode(target: Entity) {
        instance.araxxor ?: return
        // Check if we're close to Araxxor
        if (instance.araxxor!!.middleLocation.withinDistance(this.location, 5))
            instance.araxxor!!.applyHit(Hit(this, 80, HitType.DEFAULT))
        else if (instance.araxxor!!.middleLocation.withinDistance(this.location, 6))
            instance.araxxor!!.applyHit(Hit(this, 64, HitType.DEFAULT))

        // Check if we're close to the Player
        if (target.location.withinDistance(this.middleLocation, 1))
            target.applyHit(Hit(this, 49, HitType.DEFAULT))
        else if (target.location.withinDistance(this.middleLocation, 2))
            target.applyHit(Hit(this, 28, HitType.DEFAULT))
        else if (target.location.withinDistance(this.middleLocation, 3))
            target.applyHit(Hit(this, 7, HitType.DEFAULT))

        // Check if we're close to another Araxyte/Egg
        instance.araxytes.forEach {araxyte: Araxyte ->
            if (araxyte.location.withinDistance(this.middleLocation, 1))
                araxyte.applyHit(Hit(this, 80, HitType.DEFAULT))
            else if (araxyte.location.withinDistance(this.middleLocation, 2))
                araxyte.applyHit(Hit(this, 64, HitType.DEFAULT))
            else if (araxyte.location.withinDistance(this.middleLocation, 3))
                araxyte.applyHit(Hit(this, 29, HitType.DEFAULT))
        }
    }

    override fun hatchEgg(target: Entity?) {
        if (target == null) return
        this seq 11509
        this spotanim 2928
        schedule(2) {
            setTransformationPreservingStats(RUPTURA_ARAXYTE)
            addWalkSteps(instance.centerArenaTile.x, instance.centerArenaTile.y, 6)
            schedule(3) { setTarget(target) }
        }
    }

}