package com.near_reality.game.content.araxxor.rewards

import com.near_reality.game.world.entity.player.nidRaxMetamorphUnlocked
import com.zenyte.game.content.follower.Follower
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin

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
            val isNid = npc.id == NpcId.NID_13683
            val id = if (isNid) NpcId.RAX_13684 else NpcId.NID_13683
            npc.setTransformation(id)
            player.petId = id
        }
    }

    override fun getNPCs(): IntArray =
        intArrayOf(NpcId.NID_13683, NpcId.RAX_13684)
}