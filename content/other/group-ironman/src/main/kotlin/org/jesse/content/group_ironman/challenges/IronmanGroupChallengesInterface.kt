package org.jesse.content.group_ironman.challenges

import org.jesse.content.group_ironman.IronmanGroup
import org.jesse.content.group_ironman.player.finalisedIronmanGroup
import org.jesse.game.content.challenges.ChallengesManager
import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface
import org.jesse.game.util.AccessMask
import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.dialogue.CountDialogue
import mgi.types.config.StructDefinitions

@Suppress("unused")
class IronmanGroupChallengesInterface : Interface() {

    override fun open(player: Player) {
        player.varManager.sendVar(261, 10301)//10301

        var tasksFinishedFully = 0
        var tasksFinished = 0
        for (challenge in ChallengesManager.registries) {
            for (struct in challenge.value.structList) {
                val finishedCount = ChallengesManager.checkCompleted(IronmanGroup::class, struct)
                if (finishedCount >= 3) {
                    tasksFinishedFully = tasksFinishedFully or (1 shl StructDefinitions.get(struct).getParamAsInt(5014))
                } else if (finishedCount > 0) {
                    tasksFinished = tasksFinished or (1 shl StructDefinitions.get(struct).getParamAsInt(5014))
                }
            }
        }

        player.varManager.sendVar(262, tasksFinishedFully)
        player.varManager.sendVar(263, tasksFinished)

        super.open(player)
        player.packetDispatcher.sendComponentSettings(id, getComponent("List"), 0, 20, AccessMask.CLICK_OP1)
        player.finalisedIronmanGroup?.run(ChallengesManager::cashInChallenge)
        player.awaitInputInt(ListSelectDialog())
    }

    private class ListSelectDialog : CountDialogue {
        override fun run(amount: Int) {}
        override fun execute(player: Player, struct: Int) {
            val transmit = ChallengesManager.get(IronmanGroup::class, struct)
            player.packetDispatcher.sendClientScript(10628, struct, transmit)
        }
    }

    override fun attach() {
        put(8, "List")
    }

    override fun build() {
        bind("List") { player: Player, _: Int, _: Int, _: Int ->
            player.awaitInputInt(ListSelectDialog())
        }
    }

    override fun getInterface(): GameInterface  =
        GameInterface.CHALLENGES
}
