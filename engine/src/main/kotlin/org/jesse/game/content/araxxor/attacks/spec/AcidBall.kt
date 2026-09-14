package org.jesse.game.content.araxxor.attacks.spec

import org.jesse.game.content.South
import org.jesse.game.content.araxxor.Araxxor
import org.jesse.game.content.araxxor.AraxxorInstance
import org.jesse.game.content.araxxor.attacks.Attack
import org.jesse.game.content.offset
import org.jesse.game.content.seq
import org.jesse.game.util.Direction
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-25
 */
class AcidBall(
    val instance: AraxxorInstance
): NPC(
    ACIDIC_ARAXYTE_BALL,
    instance.araxxor?.middleLocation,
    true
), Attack {

    private val projectileBlob: Projectile = Projectile(2924, 256, 128, 64, 0)
    private var moveDirection: Direction = South

    init {
        radius = 0
        isRun = true
    }

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null || araxxor.isDead || araxxor.isFinished) return
        araxxor seq 11484
        val spawnLocation = araxxor.middleLocation offset Pair(-1, 0)
        this.setLocation(spawnLocation)
        this.spawn()
        moveDirection = getMovingDirection(target.location, middleLocation offset Pair(-1, 0))
        faceDirection(moveDirection)
    }

    private fun getMovingDirection(from: Location, to: Location): Direction {
        val x = from.x - to.x
        val y = from.y - to.y

        if (x < 0 && y == 0)
            return Direction.WEST
        else if (x > 0 && y == 0)
            return Direction.EAST

        else if (x == 0 && y < 0)
            return Direction.SOUTH
        else if (x == 0 && y > 0)
            return Direction.NORTH

        else if (x < 0 && y < 0)
            return Direction.SOUTH_WEST
        else if (x > 0 && y < 0)
            return Direction.SOUTH_EAST

        else if (x < 0)
            return Direction.NORTH_WEST
        else
            return Direction.NORTH_EAST
    }

    private fun explode() {
        this.remove()
        repeat(9) {
            val landing = instance.getAcidSplatterLocation(location)
            val delay = World.sendProjectile(location, landing, projectileBlob)
            World.sendGraphics(Graphics(2923, delay, 0), landing)
            instance.spawnAcidPool(landing)
        }
    }

    override fun processNPC() {
        super.processNPC()
        val nextLoc: Location = middleLocation.transform(moveDirection.offsetX, moveDirection.offsetY)
        val locAfter: Location = nextLoc.transform(moveDirection.offsetX, moveDirection.offsetY)
        instance.spawnAcidPool(middleLocation)
        val target = instance.araxxor?.attacking
        if (target != null && target is Player) {
            if (location.withinDistance(target.location, 2))
                target.applyHit(Hit(4, HitType.VENOM))
        }
        addWalkStep(locAfter.x, locAfter.y, nextLoc.x, nextLoc.y, false)
        val hitExternalBoundary = World.getObjectWithId(locAfter, 42600) != null

        if(!instance.area.inside(nextLoc)) {
            this.remove()
            return
        }

        if(hitExternalBoundary)  {
            this.remove()
            return
        }

        if (World.getObjectWithId(locAfter, 6926) != null ||
            World.getObjectWithId(locAfter, 54165) != null ||
            World.getObjectWithId(locAfter, 54166) != null ||
            World.getObjectWithId(locAfter, 54167) != null ||
            World.getObjectWithId(locAfter, 54168) != null ||
            World.getObjectWithId(locAfter, 54172) != null ||
            World.getObjectWithId(locAfter, 54173) != null ||
            World.getObjectWithId(locAfter, 54247) != null ||
            World.getObjectWithId(locAfter, 54248) != null ||
            World.getObjectWithId(locAfter, 54250) != null ||
            World.getObjectWithId(locAfter, 54251) != null ||
            World.getObjectWithId(locAfter, 54254) != null ||
            World.getObjectWithId(locAfter, 54256) != null ||
            World.getObjectWithId(locAfter, 54258) != null)
            explode()
    }

    override fun setRespawnTask() {}
}