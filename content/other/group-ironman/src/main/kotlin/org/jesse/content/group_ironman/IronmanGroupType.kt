package org.jesse.content.group_ironman

import org.jesse.game.model.ui.chat_channel.ChatChannelType
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.privilege.GameMode

enum class IronmanGroupType(
    val formattedName: String,
    val gameMode: GameMode,
    val channelType: ChatChannelType,
    val helmetId: Int,
) {
    NORMAL(
        "Group Iron",
        GameMode.GROUP_IRON_MAN,
        ChatChannelType.IronGroup,
        GROUP_IRONMAN_HELM,
    ),
    HARDCORE(
        "Hardcore Group Iron",
        GameMode.GROUP_HARDCORE_IRON_MAN,
        ChatChannelType.HardcoreIronGroup,
        HARDCORE_GROUP_IRONMAN_HELM
    );

    companion object {
        val values = values()
    }

}
