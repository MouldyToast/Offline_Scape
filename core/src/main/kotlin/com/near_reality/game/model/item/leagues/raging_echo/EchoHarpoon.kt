package com.near_reality.game.model.item.leagues.raging_echo

import com.near_reality.game.world.entity.player.echoHarpoonBanking
import com.near_reality.game.world.entity.player.echoHarpoonCookingFish
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-21
 */
class EchoHarpoon: ItemPlugin() {

    private fun Player.sendToggleMessage() =
        sendMessage("Your echo harpoon will now ${
            if(echoHarpoonCookingFish) "now cook" else "no longer cook"
        } fish for you.")

    private fun Player.sendToggleBankingMessage() =
        sendMessage("Your echo harpoon will ${
            if(echoHarpoonBanking) "now automatically bank" else "no longer bank"
        } fish for you.")

    override fun handle() {
        bind("Toggle") { player, _, _ ->
            player.dialogue {
                options {
                    "Toggle Banking" {
                        val current: Boolean = player.echoHarpoonBanking
                        player.echoHarpoonBanking = !current
                        player.sendToggleBankingMessage()

                    }
                    "Toggle Passive" {
                        val current: Boolean = player.echoHarpoonCookingFish
                        player.echoHarpoonCookingFish = !current
                        player.sendToggleMessage()
                    }
                }
            }
        }
    }

    override fun getItems(): IntArray = intArrayOf(ItemId.ECHO_HARPOON)

}