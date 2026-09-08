package org.jesse.plugins.area.osrs_home.npc

import org.jesse.game.content.treasuretrails.TreasureTrail
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class AbbotLangleyNpcaction : NPCActionScript() {

    init {
        npcs(ABBOT_LANGLEY)

        "Talk-To" {
            if (!TreasureTrail.talk(player, npc)) {
                player.dialogueManager.start(AbbotLangleyDialogue(player, npc))
            }
        }
    }
}
