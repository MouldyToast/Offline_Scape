package com.near_reality.game.content.slayer.dialogue

import com.zenyte.game.content.skills.slayer.slayer
import com.near_reality.game.content.slayer.*
import com.near_reality.game.content.slayer.SlayerMaster
import com.near_reality.game.content.slayer.SlayerMaster.SUMONA
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.region.GlobalAreaManager.getArea
import java.util.*

class SumonaAssignmentD(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        val master = SlayerMaster.mappedMasters[npc.id] ?: return
        val slayer = player.slayer()
        val currentTask = slayer.assignment
        if (currentTask != null) {
            val clazz = currentTask.area
            if (master.isKonar() && clazz != null) {
                val area = getArea(clazz)!!
                npc(
                    "You're still bringing balance to " + currentTask.task.toString()
                        .lowercase(Locale.getDefault()) + " in the " + area.name() + ", with " + currentTask.amount + " to go. Come back when you're finished."
                )
            } else {
                npc(
                    "You're still hunting " + currentTask.task.toString()
                        .lowercase(Locale.getDefault()) + "; you have " + currentTask.amount + " to go. Come back when you've finished your task."
                )
            }
            return
        }

        if (player.underSumonaReqs()) {
            npc("Sorry, but you're not skilled enough to be taught by<br><br>me. Your best trainer would be " + player.slayer().advisedMaster + ".")
            return
        }

        if (player.underSumonaGP()) {
            npc("Sorry, but for me to assign you a boss task will cost you 300,000 coins.")
            return
        }

        player.inventory.deleteItem(Item(ItemId.COINS_995, SUMMONA_TASK_COST))
        val task = player.generateSumonaTask()
        player setSlayerMaster SUMONA
        player setTask task
        npc("Your new task is to kill " + task.amount + " " + task.task.toString() + ".")
        options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne {
            setKey(100)
        }
        npc(100, task.task.tip)
    }

    companion object {
        const val SUMMONA_TASK_COST = 300000
    }
}
