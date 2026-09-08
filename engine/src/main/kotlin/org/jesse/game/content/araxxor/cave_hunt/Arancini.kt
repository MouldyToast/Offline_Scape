package org.jesse.game.content.araxxor.cave_hunt

import org.jesse.game.content.follower.impl.BossPet
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-16
 */
class Arancini: NPCPlugin() {
    override fun handle() {
        bind("Talk-to") { player, npc ->
            if (player.follower != null) {
                if (player.follower.pet.petId() == BossPet.NID.petId ||
                    player.follower.pet.petId() == BossPet.RAX.petId) {
                    (player.mapInstance as AraxyteCaveHunt).roomCompleted = true
                    npc.forceTalk = ForceTalk("Thank you for bringing me a friend.")
                }
            }
            else
                npc.forceTalk = ForceTalk("Lonely.")
        }
    }

    override fun getNPCs(): IntArray = intArrayOf(ARANCINI)
}