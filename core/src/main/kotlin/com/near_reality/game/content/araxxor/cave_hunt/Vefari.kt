package com.near_reality.game.content.araxxor.cave_hunt

import com.zenyte.game.item.ids.*
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.npc.ids.*
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-15
 */
class Vefari: NPCPlugin() {
    override fun handle() {
        bind("Talk-to") { player, npc ->
            if ((player.mapInstance as AraxyteCaveHunt).roomCompleted) {
                player.dialogue { npc("Go.") }
                return@bind
            }
            if (player.equipment.containsAnyOf(ARAXYTE_SLAYER_HELMET, ARAXYTE_SLAYER_HELMET_I)) {
                (player.mapInstance as AraxyteCaveHunt).roomCompleted = true
                npc.forceTalk = ForceTalk("It's a step in the right direction, proceed.")
            }
            else
                npc.forceTalk = ForceTalk("Boots alone do not make an Araxyte.")
        }
    }

    override fun getNPCs(): IntArray = intArrayOf(VEFARI)
}