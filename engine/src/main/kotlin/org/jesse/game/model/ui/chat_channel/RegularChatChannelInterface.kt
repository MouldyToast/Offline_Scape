package org.jesse.game.model.ui.chat_channel

import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface

/**
 * Formerly named `ChatHeaderTabInterface`
 *
 * @author Tommeh
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class RegularChatChannelInterface : Interface() {

    override fun attach() {
        put(2, "Chat-channel")
        put(3, "Your Clan")
        put(4, "View another clan")
        put(5, "Grouping")
    }

    override fun build() {
        bind("Chat-channel", ChatChannelType.ChatChannel)
        bind("Your Clan", ChatChannelType.YourClan)
        bind("View another clan", ChatChannelType.ViewAnotherClan)
        bind("Grouping", ChatChannelType.Grouping)
    }

    private fun bind(componentName: String, tabType: ChatChannelType) =
        bind(componentName) { player -> player.selectedChatChannelType = tabType }

    override fun getInterface() =
        GameInterface.REGULAR_CHAT_CHANNELS
}
