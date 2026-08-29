package com.near_reality.game.model.item.leagues.raging_echo

import com.near_reality.game.world.entity.player.echoAxeBanking
import com.near_reality.game.world.entity.player.echoAxeBurningLogs
import com.zenyte.game.content.skills.firemaking.Firemaking
import com.zenyte.game.content.skills.fletching.FletchingDefinitions
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-21
 */
class EchoAxe: ItemPlugin() {
    /**
     * The Echo axe is an item that was obtainable the Raging Echoes League, given to players at a bank upon unlocking the Lumberjack relic.
     *
     * The echo axe has no requirement to use or equip, and functions as a non-degradable version of the crystal axe.
     * In addition, on failing to chop a tree, there will be a separate 50% chance to succeed,
     * and any items gathered from Woodcutting are automatically sent to the player's bank.
     *
     *
     * For Quests needing logs, you will want to bring an alternative axe, as this axe has no way of depositing logs into your inventory.
     *
     * The echo axe also has two toggle-able effects:
     * <l>
     *  - Automatically burning logs for Firemaking experience regardless of the player's Firemaking level. This option is set by default.
     *  - Automatically Fletching logs into arrow shafts for Fletching experience regardless of the player's Fletching level. In Wintertodt, the axe will automatically fletch logs into kindling.
     * </l>
     */

    companion object {

        fun Player.processChoppedLog(logId: Int) {
            val trainingFiremaking = echoAxeBurningLogs
            val log = Item(logId, 1)
            // If we're burning the log get the firemaking exp for the given log
            if (trainingFiremaking) {
                val logData = Firemaking.getByLogId(log.id)
                val logExp = logData.xp
                getSkills().addXp(SkillConstants.FIREMAKING, logExp)
                sendFilteredMessage("Your axe burned the ${logData.logs.name} log for $logExp firemaking experience.")
            }
            // otherwise we're fletching the log into arrow shafts
            else {
                val category = getNumericTemporaryAttribute("LogsCategory").toInt()
                val product = FletchingDefinitions.PRODUCTS[category][0]
                val exp = FletchingDefinitions.EXPERIENCE[category][0]
                getSkills().addXp(SkillConstants.FLETCHING, exp)
                inventory.addItem(product)
                sendFilteredMessage("Your axe fletched the ${log.name} log into arrow shafts for $exp fletching experience.")
            }
        }
    }

    private fun Player.sendToggleMessage()  =
        sendMessage("Your echo axe will now automatically ${
            if(echoAxeBurningLogs) "burn" else "fletch"
        } logs for you.")

    private fun Player.sendToggleBankingMessage()  =
        sendMessage("Your echo axe will ${
            if(echoAxeBanking) "now automatically bank" else "no longer bank"
        } logs for you.")

    override fun handle() {
        bind("Toggle") { player, _, _ ->
            player.dialogue {
                options {
                    "Toggle Banking" {
                        val current: Boolean = player.echoAxeBanking
                        player.echoAxeBanking = !current
                        player.sendToggleBankingMessage()

                    }
                    "Toggle Passive" {
                        val current: Boolean = player.echoAxeBurningLogs
                        player.echoAxeBurningLogs = !current
                        player.sendToggleMessage()
                    }
                }
            }
        }
    }

    override fun getItems(): IntArray = intArrayOf(ItemId.ECHO_AXE)

}