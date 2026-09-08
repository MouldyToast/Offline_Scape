package org.jesse.game.content.boss.abyssalsire.poisonfumes

import org.jesse.game.content.boss.abyssalsire.AbyssalSire
import org.jesse.game.content.boss.abyssalsire.AbyssalSirePhase
import org.jesse.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import org.jesse.game.world.World
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSirePoisonFumes(val sire: AbyssalSire) {

    private val collection: MutableCollection<AbyssalSirePoisonFume> = HashSet()

    fun attemptClear() {
        if (collection.isNotEmpty()) {
            collection.removeIf { it.remove(sire) }
        }
    }

    fun size() = collection.size

    fun sendPoisonFume() = sire.target { target ->
//        val middle = sire.middleLocation
//        val destination = ImmutableLocation(target.location)
//        val distance = destination.getTileDistance(middle)
//
//        if (distance > AbyssalSire.MAXIMUM_ATTACK_DISTANCE
//            || middle.plane != destination.plane
//        ) return@target
//
//        if (collection.add(AbyssalSirePoisonFume(destination))) {
//            if (sire.phase.animates)
//                sire.animation = shootingPoisonFumesAnimation
//            World.sendGraphics(poisonFumesGraphics, destination)
//        }
    }

    private companion object {
        val shootingPoisonFumesAnimation = Animation(4531)
        val poisonFumesGraphics = Graphics(1275)
    }

}
