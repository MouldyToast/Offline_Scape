package org.jesse.game.content.theatreofblood.plugin.npc

import org.jesse.game.content.theatreofblood.plugin.dialogue.MysteriousStrangerDialogue
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.VarManager

/**
 * @author Jire
 * @author Tommeh
 */
class MysteriousStranger : NPCPlugin() {

    override fun handle() {
        bind("Talk-to", object : OptionHandler {
            override fun handle(player: Player, npc: NPC) = startInitialDialogue(player, npc.id)

            override fun execute(player: Player, npc: NPC) {
                player.stopAll()
                player.setFaceEntity(npc)
                handle(player, npc)
            }
        })
        bind("Trade", object : OptionHandler {
            override fun handle(player: Player, npc: NPC) = player.openShop("Mysterious Stranger")

            override fun execute(player: Player, npc: NPC) {
                player.stopAll()
                player.setFaceEntity(npc)
                handle(player, npc)
            }
        })
    }

    override fun getNPCs() = npcs

    internal companion object {

        const val DIALOGUE_VARBIT = 12973

        fun completedInitialDialogue(player: Player) =
            player.varManager.getBitValue(DIALOGUE_VARBIT) == 1

        private val npcs = intArrayOf(MYSTERIOUS_STRANGER, 10875, 10876) // TODO

        fun startInitialDialogue(player: Player, npcID: Int = npcs[0]) =
            player.dialogueManager.start(MysteriousStrangerDialogue(player, npcID))

        init {
            VarManager.appendPersistentVarbit(DIALOGUE_VARBIT)
        }

    }

}