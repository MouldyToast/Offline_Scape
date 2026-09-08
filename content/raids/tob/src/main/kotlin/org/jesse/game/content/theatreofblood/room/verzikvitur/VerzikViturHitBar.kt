package org.jesse.game.content.theatreofblood.room.verzikvitur

import org.jesse.game.world.entity.EntityHitBar

/**
 * @author Jire
 */
internal class VerzikViturHitBar(
    private val verzikVitur: VerzikVitur
) : EntityHitBar(verzikVitur) {

    override fun getType() =
        if (verzikVitur.phase == VerzikViturPhase.FIRST) 10
        else super.getType()

}