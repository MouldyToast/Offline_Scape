package org.jesse.game.content.theatreofblood.room.maidenofsugadinti

import org.jesse.game.content.theatreofblood.room.maidenofsugadinti.npc.MaidenOfSugadinti
import org.jesse.game.npc.ids.*

/**
 * @author Tommeh
 * @author Jire
 */
internal enum class MaidenOfSugadintiPhase(val percent: Int, val npcId: Int) {

    FIRST(100, THE_MAIDEN_OF_SUGADINTI),
    SECOND(70, THE_MAIDEN_OF_SUGADINTI_8361),
    THIRD(50, THE_MAIDEN_OF_SUGADINTI_8362),
    FOURTH(30, THE_MAIDEN_OF_SUGADINTI_8363),
    DYING(0, THE_MAIDEN_OF_SUGADINTI_8364),
    DEAD(0, THE_MAIDEN_OF_SUGADINTI_8365);

    companion object {

        private val phases: Array<MaidenOfSugadintiPhase> = entries.toTypedArray()

        val MaidenOfSugadinti.appropriateNewPhase: MaidenOfSugadintiPhase
            get() {
                val hitpointsAsPercentage = hitpointsAsPercentage
                for (i in phases.indices.reversed()) {
                    val phase = phases[i]
                    if (hitpointsAsPercentage <= phase.percent)
                        return phase
                }
                return DYING
            }

    }

}