package com.zenyte.game.content.boss.abyssalsire.actions

import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusArea
import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusCorner
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.region.GlobalAreaManager
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

class EyeNpcaction : NPCActionScript() {

    init {
        npcs(EYE)

        "Peek" {
        	var area = AbyssalNexusCorner.NORTH_EAST
        	if (npc.location.equals(3027, 4786, 0)) {
        		area = AbyssalNexusCorner.NORTH_WEST
        	} else if (npc.location.equals(3024, 4770, 0)) {
        		area = AbyssalNexusCorner.SOUTH_WEST
        	} else if (npc.location.equals(3051, 4771, 0)) {
        		area = AbyssalNexusCorner.SOUTH_EAST
        	}
        	val lair = GlobalAreaManager[area.areaName] as AbyssalNexusArea
        	val size = lair.players.size
        	if (size == 0) {
        		player.dialogue { plain("There are no adventurers in this chamber.") }
        	} else if (size == 1) {
        		player.dialogue { plain("There is 1 adventurer in this chamber.") }
        	} else {
        		player.dialogue { plain("There are $size adventurers in this chamber.") }
        	}
        }
    }
}
