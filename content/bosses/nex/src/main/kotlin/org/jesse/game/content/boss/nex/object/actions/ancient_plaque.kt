package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.GameInterface
import org.jesse.game.content.ItemRetrievalService
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class AncientPlaqueObjectaction : ObjectActionScript() {

    init {
        ANCIENT_PLAQUE_42935 {
            when(option) {
                "Read" -> {
                    player.interfaceHandler.sendInterface(GameInterface.STONE_TEXT_INTERFACE)
                    player.packetDispatcher.sendComponentText(GameInterface.STONE_TEXT_INTERFACE, 5, "<br><br><br><br><br><col=D1C06E>Vita brevis breviter in brevi finietur,<br><col=D1C06E>Mors venit velociter quae neminem veretur,<br><col=D1C06E>Omnia mors perimit et nulli miseretur.")
                }
            }
        }
    }
}
