package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.content.theatreofblood.interfaces.PartiesOverviewInterface.Companion.refresh
import org.jesse.game.content.theatreofblood.plugin.npc.MysteriousStranger
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Tommeh
 * @author Jire
 */
class NoticeboardObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (option == "Read") {
            if (!MysteriousStranger.completedInitialDialogue(player)) {
                // TODO add Mysterious Stranger "Talk-to" option
                //player.dialogueManager.start(NPCChat(player, NpcId.MYSTERIOUS_STRANGER_8325, "Hey. Over here."))
                MysteriousStranger.startInitialDialogue(player)
                return
            }
            refresh(player)
        }
    }

    override fun getObjects() = NoticeboardObject.objects

    private companion object {

        val objects = arrayOf(32655)

    }

}