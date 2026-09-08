package org.jesse.game.content.araxxor.rewards

import org.jesse.game.world.entity.player.nidRaxMetamorphUnlocked
import org.jesse.game.content.follower.Follower
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-02
 */
class NidRaxTransMorph : NPCPlugin() {
    override fun handle() {
        bind("Metamorph") {
            player, npc ->
            if (npc !is Follower || npc.owner != player) {
                player.sendMessage("This is not your pet.")
                return@bind
            }
            if (!player.nidRaxMetamorphUnlocked) {
                player.sendMessage("You have not learned how to do this yet.")
                return@bind
            }
            val isNid = npc.id == NID_13683
            val id = if (isNid) RAX_13684 else NID_13683
            npc.setTransformation(id)
            player.petId = id
        }
    }

    override fun getNPCs(): IntArray =
        intArrayOf(NID_13683, RAX_13684)
}