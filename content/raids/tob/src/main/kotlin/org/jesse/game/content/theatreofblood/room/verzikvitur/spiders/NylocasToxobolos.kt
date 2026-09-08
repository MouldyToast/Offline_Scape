package org.jesse.game.content.theatreofblood.room.verzikvitur.spiders

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.HitType

/**
 * @author Jire
 */
internal class NylocasToxobolos(room: VerzikViturRoom, location: Location) : NylocasChaseCrab(room, ID, location) {

    override val styleToAttack = HitType.RANGED

    companion object {
        const val ID = 8382
    }

}