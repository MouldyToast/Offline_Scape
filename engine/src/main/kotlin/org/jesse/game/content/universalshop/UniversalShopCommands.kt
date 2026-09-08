package org.jesse.game.content.universalshop

import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

object UniversalShopCommands {
    fun register() {
        GameCommands.Command(PlayerPrivilege.MODERATOR, "usm") { player, _ ->
            player.dialogueManager.start(MainMenu(player))
        }
    }
}