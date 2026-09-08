package org.jesse.content.group_ironman.widget

import org.jesse.content.group_ironman.IronmanGroup
import org.jesse.content.group_ironman.dialogue.CreateIronmanGroupDialogue
import org.jesse.content.group_ironman.player.inIronManGroup
import org.jesse.content.group_ironman.player.inIronmanGroupCreationInterface
import org.jesse.game.model.ui.chat_channel.chatChannelInterfaceType
import org.jesse.game.util.component
import org.jesse.scripts.interfaces.InterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface
import org.jesse.game.GameInterface.*
import org.jesse.game.util.AccessMask
import org.jesse.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class TabGimDefaultInterface : InterfaceScript() {

    init {
        DEFAULT_GIM_TAB {
            "Create Group"(3) {
                IronmanGroup.form(player, prestige = false)
                //player.dialogueManager.start(CreateIronmanGroupDialogue(player))
            }
            opened {
                inIronmanGroupCreationInterface = false
                if (inIronManGroup)
                    ACTIVE_GIM_TAB.open(this)
                else {
                    chatChannelInterfaceType.sendTabInterface(this, getInterface())
                    packetDispatcher.run {
                        sendClientScript(5261, id component 3, 1, id component 1, "Iron Group")
                        sendClientScript(
                            5261, id component 3, 1, id component 5,
                            "To be part of an Iron Group, you must either get invited to a group or start your own. " +
                                    "The button below will start a new group with you as the leader."
                        )
                    }
                }
            }
        }
    }
}
