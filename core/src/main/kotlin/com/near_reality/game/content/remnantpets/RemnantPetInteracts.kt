package com.near_reality.game.content.remnantpets

import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import java.awt.Color

class RemnantPetInteracts : NPCPlugin() {

    override fun handle() {
        bind("Restore") { player: Player, npc: NPC ->
            if(player.follower.index != npc.index) {
                player.sendMessage("You can not activate this on someone else's pet")
                return@bind
            }

            if(!player.follower.isRemnantPet()) {
                player.sendMessage("You can not activate this on a non-remnant pet")
                return@bind
            }

            player.remnantPetManager.attemptRestore()
        }
        bind("Toggle") { player: Player, npc: NPC ->
            if(player.follower.index != npc.index) {
                player.sendMessage("You can not activate this on someone else's pet")
                return@bind
            }

            if(!player.follower.isRemnantPet()) {
                player.sendMessage("You can not activate this on a non-remnant pet")
                return@bind
            }
            if(player.selectedImpPerkIsExtend) {
                player.selectedImpPerkIsExtend = false
                player.sendMessage(Colour.GREEN.wrap("Your pet is now helping you speed up your slayer tasks"))
            } else {
                player.selectedImpPerkIsExtend = true
                player.sendMessage(Colour.GREEN.wrap("Your pet is now helping you slow down your slayer tasks"))
            }
        }
    }

    override fun getNPCs() = RemnantPetManager.registeredPets.filter{ it.npcId != -1 }.map { it.npcId }.toIntArray()
}