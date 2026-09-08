package org.jesse.content.group_ironman.dialogue

import org.jesse.api.service.user.UserPlayerHandler
import org.jesse.content.group_ironman.IronmanGroupType
import org.jesse.content.group_ironman.player.ironmanGroupType
import org.jesse.game.model.ui.chat_channel.selectedChatChannelType
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Represents a [Dialogue] for changing the [player]'s [group iron man mode][IronmanGroupType].
 *
 * This dialogue can be opened by using one of the armour crates at The Node.
 */
class ChangeGroupIronManModeDialogue(
    player: Player,
    private val newGroupType: IronmanGroupType
) : Dialogue(player) {

    override fun buildDialogue() {
        options("Would you like to change to ${newGroupType.formattedName} mode?") {
            "Yes." {
                player.animation = Animation.GRAB
                val previousGroupType = player.ironmanGroupType
                UserPlayerHandler.updateGameMode(player, newGroupType.gameMode) { success ->
                    if (success) {
                        player.selectedChatChannelType = newGroupType.channelType
                        if (previousGroupType != null) {
                            player.equipment.deleteItem(previousGroupType.helmetId, 1)
                            player.inventory.deleteItem(previousGroupType.helmetId, 1)
                        }
                        player.inventory.addItem(newGroupType.helmetId, 1)
                    }
                }
            }
            "No." {}
        }
    }
}
