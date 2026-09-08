package org.jesse.game.content.theatreofblood.room.verzikvitur.spiders

import org.jesse.game.content.theatreofblood.room.TheatreNPC
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.combat.CombatScript

/**
 * @author Jire
 */
internal class NylocasMatomenos(room: VerzikViturRoom, location: Location) :
    TheatreNPC<VerzikViturRoom>(room, ID, location), CombatScript {

    override fun attack(target: Entity?) = 0

    override fun autoRetaliate(source: Entity?) {}

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)

        if (hit.hitType == HitType.SHIELD) {
            hit.damage = 0
        }
    }

    companion object {
        const val ID = 8385
    }

}