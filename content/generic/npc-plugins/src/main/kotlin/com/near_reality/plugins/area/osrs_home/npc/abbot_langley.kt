package com.near_reality.plugins.area.osrs_home.npc

import com.zenyte.game.content.treasuretrails.TreasureTrail
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

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
