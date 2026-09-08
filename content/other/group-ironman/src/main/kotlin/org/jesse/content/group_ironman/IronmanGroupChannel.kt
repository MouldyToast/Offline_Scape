package org.jesse.content.group_ironman

import org.jesse.content.group_ironman.player.sendChatChannelPacket
import org.jesse.content.group_ironman.player.sendChatChannelSettingsPacket
import org.jesse.game.model.ui.chat_channel.ChatChannelRank
import org.jesse.game.net.packet.ChatChannelType
import org.jesse.game.net.packet.ChatChannelVar
import org.jesse.game.world.entity.masks.ChatMessage
import org.jesse.game.world.entity.player.MessageType
import org.jesse.game.world.entity.player.Player
import org.jesse.utils.TextUtils
import net.rsprot.protocol.game.outgoing.clan.ClanChannelFull
import net.rsprot.protocol.game.outgoing.clan.ClanSettingsFull

/**
 * Handles the chat channel mechanics for the argued [group].
 *
 * @author Leanbow, Stan van der Bend
 */
class IronmanGroupChannel(private val group: IronmanGroup) {

    /**
     * Represents the currently online group [IronmanGroup.activeMembers].
     */
    private val membersInChannel: MutableList<Player> by lazy { mutableListOf() }

    /**
     * Adds the [player] to [membersInChannel].
     */
    fun add(player: Player, update: Boolean = true) {
        player.delay(1) {
            player.sendMessage(IronmanGroup.JOIN_GIM_CHANNEL_MESSAGE, MessageType.CLAN)
        }
        membersInChannel.add(player)
        if (update) {
            refreshChannel()
        }
    }

    /**
     * Removes the [player] from [membersInChannel].
     */
    fun remove(player: Player) {
        membersInChannel.remove(player)
        refreshChannel()
    }

    /**
     * Set the argued [chatChannelVar] to [value] in [variables] and
     * updates the chat channel settins for the online members.
     */
    fun setVariableInt(chatChannelVar: ChatChannelVar, value: Int) {
        group.variables[chatChannelVar] = value
        membersInChannel.forEach { player ->
            player.sendChatChannelSettingsPacket(group)
        }
    }

    fun refreshChannel() {
        membersInChannel.forEach { player ->
            player.sendChatChannelPacket(group)
        }
    }

    /**
     * Sends the [message] to all [membersInChannel].
     */
    fun sendPlayerMessage(message: String, name: String) {
        membersInChannel.forEach { member ->
            member.packetDispatcher.sendClanChannelMessage(
                ChatChannelType.GIM,
                TextUtils.formatName(name),
                message)
        }
    }

    fun sendMessage(message: String) {
        membersInChannel.forEach { member ->
            member.sendMessage("|$message", MessageType.CLAN)
        }
    }

    fun toSettingsPacket(): ClanSettingsFull.Update {
        val settingsList = emptyList<ClanSettingsFull.ClanSetting>() // Default; replace with your actual settings if any.
        val settingsUpdateNum = 0 // Default; replace with your actual settings update counter.
        // Convert creationTime to seconds.
        val creationTimeSec = (group.creationTime.toEpochMilliseconds() / 1000).toInt()
        // Map active members to affined clan members.
        val affinedMembers = group.activeMembers.map { member ->
            // Use the constructor with (hash, name, rank, extraInfo, joinRuneDay, muted)
            ClanSettingsFull.AffinedClanMember(
                hash = 0L,
                name = TextUtils.formatName(member.username),
                rank = ChatChannelRank.CLAN_RANK_1.ordinal,
                extraInfo = 0,
                joinRuneDay = 0,
                muted = false
            )
        }
        return ClanSettingsFull.JoinUpdate(
            updateNum = settingsUpdateNum,
            creationTime = creationTimeSec,
            clanName = group.name,
            allowUnaffined = false,
            talkRank = 0,
            kickRank = 0,
            lootshareRank = 0,
            coinshareRank = 0,
            affinedMembers = affinedMembers,
            bannedMembers = emptyList(),
            settings = settingsList
        )
    }

    /**
     * Returns a full channel update for the clan.
     *
     * The returned type is [ClanChannelFull.Update].
     */
    fun toUpdatePacket(): ClanChannelFull.Update {
        val hash = 0L       // Default; replace with your actual clan hash if available.
        val updateNum = 0L  // Default; replace with your actual update counter if available.
        val members = membersInChannel.map { member ->
            // Use the constructor with (name, rank, world, discardedBoolean)
            ClanChannelFull.ClanMember(
                name = member.username,
                rank = ChatChannelRank.CLAN_RANK_1.ordinal,
                world = member.worldId,
                discardedBoolean = false
            )
        }
        return ClanChannelFull.JoinUpdate(
            clanHash = hash,
            updateNum = updateNum,
            clanName = group.name,
            discardedBoolean = false,
            kickRank = 0,
            talkRank = 0,
            members = members
        )
    }
}
