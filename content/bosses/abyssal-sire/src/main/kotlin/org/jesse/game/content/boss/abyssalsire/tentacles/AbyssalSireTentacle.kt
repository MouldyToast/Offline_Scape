package org.jesse.game.content.boss.abyssalsire.tentacles

import org.jesse.game.content.boss.abyssalsire.AbyssalSire
import org.jesse.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import org.jesse.game.util.CollisionUtil
import org.jesse.game.util.Direction
import org.jesse.game.util.DirectionUtil
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities.delayHit
import org.jesse.game.world.entity.player.action.combat.CombatUtilities.getRandomMaxHit
import mgi.utilities.CollectionUtils

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireTentacle(
    private val asleepID: Int,
    location: Location,
    val sire: AbyssalSire
) : NPC(asleepID, location, Direction.SOUTH, 0) {

    private val queue = AbyssalTentacleQueue()

    var timeUntilLastBlock = 0

    fun resetTentacle() {
        queue.clear() // Forcibly clear out the queue.

        queue { setTransformation(asleepID) }
        queue { faceLocation = middleLocation.transform(Direction.SOUTH) }
        queue(1)
    }

    fun disorientate() {
        queue(2)
        queue {
            setAnimation(fallAnimation)
            setTransformation(INACTIVE_TENTACLE)
        }
        queue(4)
        queue { faceLocation = middleLocation.transform(Direction.SOUTH) }
    }

    fun riseIfNotActive() {
        if (id == ACTIVE_TENTACLE) return
        queue(3)
        queue {
            setAnimation(if (id == asleepID) wakeAnimation else riseAnimation)
            setTransformation(ACTIVE_TENTACLE)
        }
        queue(6)
    }

    fun rise() {
        queue(3)
        queue {
            setAnimation(if (id == asleepID) wakeAnimation else riseAnimation)
            setTransformation(ACTIVE_TENTACLE)
        }
        queue(6)
    }

    override fun processNPC() {
        if (queue.process()) {
            timeUntilLastBlock = ATTACK_SPEED
            return
        }

        if (id == INACTIVE_TENTACLE) return

        if (++timeUntilLastBlock < 4) return
        timeUntilLastBlock = 0

        sire.target {
            orientateTowards(it)
            if (CollisionUtil.collides(x, y, getSize(), it.x, it.y, it.size)) {
                setAnimation(whackAnimation)
                delayHit(
                    null, 0, it,
                    Hit(getRandomMaxHit(sire, MAX_HIT, AttackType.CRUSH, it), HitType.REGULAR)
                )
                sire.hitByTentacles = true
                sire.perfectSire = false
            }
        }
    }

    private fun orientateTowards(target: Player) {
        val playerCenter = target.middleLocation
        val tentacleCenter = middleLocation
        val angle = getRoundedDirection(
            DirectionUtil.getFaceDirection(
                (tentacleCenter.x - playerCenter.x).toDouble(),
                (tentacleCenter.y - playerCenter.y).toDouble()
            ), 1024
        )
        val direction = CollectionUtils.findMatching(Direction.values) { it.npcDirection == angle }!!
        faceLocation = tentacleCenter.transform(direction)
    }

    override fun sendDeath() {}

    override fun applyHit(hit: Hit) {}

    private companion object {

        const val ATTACK_SPEED = 4
        const val MAX_HIT = 30

        const val ACTIVE_TENTACLE = TENTACLE_5912
        const val INACTIVE_TENTACLE = TENTACLE_5913

        val wakeAnimation = Animation(7108)
        val fallAnimation = Animation(7112)
        val riseAnimation = Animation(7114)
        val whackAnimation = Animation(7109)

    }

}