package org.jesse.content.group_ironman.npc

import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*

/**
 * A [NPC] present in the "The Node" area that has a procedure where they mine ores,
 * and give it to [Regent] for melting.
 *
 * @author Leanbow, Stan van der Bend
 */
class R0ck5masher(private val regent: Regent) : NPC(R0CK_5MASHER, giveOresLocation, false) {

    private var sequenceIndex = MINE

    override fun processNPC() {
        if (hasWalkSteps())
            return
        if (forceWalk != null)
            calcFollow(forceWalk, -1, true, true, false)
        if (location.matches(forceWalk))
            handleSequence()
    }

    fun handleSequence() {
        when (sequenceIndex) {
            MINE -> {
                faceDirection(Direction.EAST)
                delay(1) {
                    setAnimation(Animation(626))
                    delay(2) {
                        setAnimation(null)
                        delay(2) {
                            forceWalk = giveOresLocation
                            sequenceIndex = GIVE_ORE
                        }
                    }
                }
            }
            GIVE_ORE -> {
                faceDirection(Direction.EAST)
                delay(2) {
                    setForceTalk("Here's the iron for the arrows.")
                    regent.startSequence()
                    delay(1) {
                        forceWalk = mineLocation
                        sequenceIndex = MINE
                    }
                }
            }
        }

        sequenceIndex = 0
    }

    override fun isEntityClipped(): Boolean =  false

    private companion object {
        val giveOresLocation = Location(3104, 3024, 0)
        val mineLocation = Location(3106, 3012, 0)

        const val MINE = 1
        const val GIVE_ORE = 2
    }
}
