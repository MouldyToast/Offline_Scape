package org.jesse.content.group_ironman.`object`

import org.jesse.content.group_ironman.IronmanGroupType
import org.jesse.content.group_ironman.dialogue.ChangeGroupIronManModeDialogue
import org.jesse.content.group_ironman.player.finalisedIronmanGroup
import org.jesse.content.group_ironman.player.inIronmanGroupCreationInterface
import org.jesse.content.group_ironman.player.ironmanGroupType
import org.jesse.game.item.Item
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class ArmourCrateObjectaction : ObjectActionScript() {

    fun Int.armourCrate(groupType: IronmanGroupType) = invoke {
        when {
            player.finalisedIronmanGroup != null ->
                player.dialogue(GROUP_IRONMAN_TUTOR) {
                    npc("You cannot change your Group Iron Mode as you are already part of a ${player.finalisedIronmanGroup!!.allMembers.size}-player Iron group.")
                }
            player.inIronmanGroupCreationInterface ->
                player.dialogue { plain("You can't change your game mode or get a new helmet while creating a group.") }
            player.ironmanGroupType != groupType ->
                player.dialogueManager.start(ChangeGroupIronManModeDialogue(player, groupType))
            else -> {
                val helmet = Item(groupType.helmetId, 1)
                if (player.containsItem(helmet))
                    player.dialogue { plain("You already have a helmet.") }
                else {
                    player.inventory.addItem(helmet)
                    player.dialogue { item(helmet, "You take a helmet.") }
                }
            }
        }
    }

    init {
        ARMOUR_CRATE.armourCrate(IronmanGroupType.NORMAL)
        //HARDCORE_ARMOUR_CRATE.armourCrate(IronmanGroupType.HARDCORE)
        HARDCORE_ARMOUR_CRATE.armourCrate(IronmanGroupType.HARDCORE)
    }
}
