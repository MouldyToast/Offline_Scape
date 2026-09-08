package org.jesse.game.content.theatreofblood.room.verzikvitur.first

import org.jesse.game.world.entity.TargetSwitchCause
import org.jesse.game.content.theatreofblood.room.TheatreNPC
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.masks.UpdateFlag
import org.jesse.game.world.entity.npc.NPCCombat
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet

/**
 * @author Jire
 */
internal class SupportingPillar(room: VerzikViturRoom, location: Location) :
    TheatreNPC<VerzikViturRoom>(room, NPC_ID, location) {

    val worldObject = WorldObject(32687, tile = location)
    val behindSpots: IntSet = IntOpenHashSet()

    override fun spawn() = (super.spawn() as SupportingPillar).apply {
        combat = object : NPCCombat(this) {
            override fun setTarget(
                target: Entity,
                cause: TargetSwitchCause
            ) {}
            override fun forceTarget(target: Entity) {}
        }
        setTargetType(Entity.EntityType.NPC)

        World.spawnObject(worldObject)

        fun Location.behindSpot() = behindSpots.add(positionHash)

        val location = location!!
        if (location.x == room.getBaseX(25)) {
            val southWest = location.transform(-3, -3)
            val northEast = location.transform(0, 0)
            for (x in southWest.x..northEast.x) {
                for (y in southWest.y..northEast.y) {
                    Location(x, y, location.plane).behindSpot()
                }
            }
            location.transform(1, -1).behindSpot()
            location.transform(-1, 1).behindSpot()
        } else if (location.x == room.getBaseX(37)) {
            val southWest = location.transform(2, -2)
            val northEast = location.transform(4, 0)
            for (x in southWest.x..northEast.x) {
                for (y in southWest.y..northEast.y) {
                    Location(x, y, location.plane).behindSpot()
                }
            }
            location.transform(1, -1).behindSpot()
            location.transform(3, 1).behindSpot()
        }
    }

    override fun isMovableEntity() = false

    override fun sendDeath() {
        World.removeObject(worldObject)
        val spawnLocation = location.transform(-1, -1)
        setLocation(spawnLocation)
        setTransformation(8377)
        setAnimation(Animation(8052))

        WorldTasksManager.schedule({
            for (p in room.validTargets) {
                if (middleLocation.withinDistance(p, 2)) {
                    val maxDamage = 65
                    p.applyHit(Hit(this, Utils.random(maxDamage / 2, maxDamage), HitType.REGULAR))
                    p.sendMessage("The collapsing pillar greatly harms you.")
                }
            }
            WorldTasksManager.schedule({
                setTransformation(8378)
                setAnimation(Animation(8104))
                WorldTasksManager.schedule({
                    onFinish(null)
                }, 2)
            }, 1)
        }, 1)
    }

    override fun setTransformation(id: Int) {
        nextTransformation = id
        setId(id)
        size = definitions.size
        updateFlags.flag(UpdateFlag.TRANSFORMATION)
    }

    override fun isValidAnimation(animID: Int): Boolean {
        return true
    }

    fun playerIsBehind(player: Player) = !isDead && !isFinished
            && room.isValidTarget(player)
            && behindSpots.contains(player.location.positionHash)

    companion object {

        const val NPC_ID = 8379

    }

}
