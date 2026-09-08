package org.jesse.game.content.boss.abyssalsire.respiratorysystems

import org.jesse.game.content.boss.abyssalsire.AbyssalSire
import org.jesse.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.slf4j.LoggerFactory
import java.lang.ref.WeakReference

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireRespiratorySystem(
    location: Location,
    val sireRef: WeakReference<AbyssalSire>
) : NPC(RESPIRATORY_SYSTEM, location, Direction.SOUTH, 0) {

    constructor(location: Location, sire: AbyssalSire) : this(location, WeakReference(sire))

    var state = State.NONE

    enum class State(
        val newVentID: Int = -1,
        val onSet: (AbyssalSireRespiratorySystem.(oldVent: WorldObject, newVent: WorldObject) -> Unit)? = null
    ) {
        NONE,
        GASSY(VENT_26953, { _, n -> World.spawnObject(n) }),
        SILENT(VENT_26954, { o, n ->
            World.sendObjectAnimation(o, ventFailureAnimation)
            WorldTasksManager.schedule({ World.spawnObject(n) }, 4)
        });

        fun set(respiratorySystem: AbyssalSireRespiratorySystem) {
            if (this == respiratorySystem.state) return

            try {
                val oldVent = World.getObjectOfSlot(respiratorySystem.location, 10)!!
                val newVent = WorldObject(oldVent).apply {
                    id = newVentID
                }
                onSet?.invoke(respiratorySystem, oldVent, newVent)
            } catch (e: Exception) {
                logger.error("", e)
            }

            respiratorySystem.state = this
        }
    }

    override fun spawn(): NPC {
        State.GASSY.set(this)
        return super.spawn()
    }

    override fun finish() {
        super.finish()
        State.SILENT.set(this)
    }

    override fun sendDeath() {
        WorldTasksManager.schedule(::finish)
    }

    override fun getXpModifier(hit: Hit): Float {
        if (sireRef.get()?.id == AbyssalSire.SIRE_THRONE_STUNNED_ID) return super.getXpModifier(hit)

        val hitSource = hit.source
        if (hitSource is Player)
            hitSource.sendFilteredMessage("You can't deal much damage with those tentacles getting in the way.")

        val originalDamage = hit.damage
        if (originalDamage > 3)
            hit.damage = Utils.random(1, 3)

        return super.getXpModifier(hit)
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(AbyssalSireRespiratorySystem::class.java)

        val ventFailureAnimation = Animation(7102)
    }

}
