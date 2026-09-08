package org.jesse.game.net.packet

import org.jesse.api.service.sanction.muteSanction
import org.jesse.game.world.entity.player.ironGroupMessageHandler
import org.jesse.tools.logging.GameLogMessage
import org.jesse.tools.logging.GameLogger.log
import org.jesse.game.GameConstants
import org.jesse.game.content.clans.ClanChannel
import org.jesse.game.content.clans.ClanManager
import org.jesse.game.content.clans.ClanRank
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.UpdateFlag
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.Setting
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.utils.TextUtils
import kotlinx.datetime.Clock
import mgi.utilities.StringFormatUtil
import net.rsprot.protocol.game.incoming.friendchat.FriendChatJoinLeave
import net.rsprot.protocol.game.incoming.friendchat.FriendChatKick
import net.rsprot.protocol.game.incoming.friendchat.FriendChatSetRank
import net.rsprot.protocol.game.incoming.messaging.MessagePrivate
import net.rsprot.protocol.game.incoming.messaging.MessagePublic
import net.rsprot.protocol.game.incoming.misc.user.SetChatFilterSettings
import net.rsprot.protocol.game.incoming.social.FriendListAdd
import net.rsprot.protocol.game.incoming.social.FriendListDel
import net.rsprot.protocol.game.incoming.social.IgnoreListAdd
import net.rsprot.protocol.game.incoming.social.IgnoreListDel
import org.slf4j.event.Level
import java.util.*

internal fun PacketConsumer.friendListAdd() {
    addListener<FriendListAdd> {
        var name = it.name
        if (name.length > 12) {
            name = name.substring(0, 12)
        }
        player.socialManager.addFriend(name)
    }
}

internal fun PacketConsumer.friendListDel() {
    addListener<FriendListDel> {
        val name = it.name
        player.socialManager.removeFriend(name)
    }
}

internal fun PacketConsumer.ignoreListAdd() {
    addListener<IgnoreListAdd> {
        var name = it.name
        if (name.length > 12) {
            name = name.substring(0, 12)
        }
        name = name.replace(" ".toRegex(), "_")
        player.socialManager.addIgnore(name)
    }
}

internal fun PacketConsumer.ignoreListDel() {
    addListener<IgnoreListDel> {
        var name = it.name
        name = name.lowercase(Locale.getDefault()).replace(" ".toRegex(), "_")
        player.socialManager.removeIgnore(name)
    }
}

internal fun PacketConsumer.fcJoinLeave() {
    addListener<FriendChatJoinLeave> {
        val name = it.name
        val player = player
        val current = player.settings.channel
        if (current == null) {
            if (name.isNullOrEmpty()) {
                return@addListener
            }
            ClanManager.join(player, name)
        } else {
            ClanManager.leave(player, true)
        }
    }
}

internal fun PacketConsumer.fcKick() {
    addListener<FriendChatKick> {
        val target = World.getPlayer(it.name)
        if (!target.isPresent) {
            return@addListener
        }
        ClanManager.kick(player, true, target.get(), false)
    }
}

internal fun PacketConsumer.fcSetRank() {
    addListener<FriendChatSetRank> {
        val name = it.name.trim()
        val rank = it.rank

        val player = player

        val channel: ClanChannel = ClanManager.getChannel(player) ?: return@addListener
        val clanRank = ClanRank.getRank(rank) ?: return@addListener
        val loggedInPlayer = World.getPlayer(name)
        if (loggedInPlayer.isEmpty) {
            channel.rankedMembers[StringFormatUtil.formatUsername(name)] = clanRank
            player.packetDispatcher.initFriendsList()
        } else {
            channel.rankedMembers[StringFormatUtil.formatUsername(name)] = clanRank
            if (channel.members.contains(loggedInPlayer.get())) {
                ClanManager.refreshPartial(channel, loggedInPlayer.get(), true, false)
            }
            player.packetDispatcher.initFriendsList()
        }
    }
}

internal fun PacketConsumer.messagePrivate() {
    addListener<MessagePrivate> {
        val recipient = it.name.trim()
        val message = it.message

        val player = player

        val mute = player.muteSanction
        if (mute != null) {
            player.sendMessage("You cannot talk while the punishment is active:<br>" + mute.format() + ".")
        } else {
            player.socialManager.sendMessage(recipient, message)
        }
        if (GameConstants.WORLD_PROFILE.isLogsDatabaseEnabled())
            log(Level.INFO) {
                GameLogMessage.Message.Private(
                    Clock.System.now(),
                    player.dbUsername,
                    recipient,
                    message
                )
            }
    }
}


enum class PublicChatType {
    NONE,
    QUICKCHAT,
    CLANCHAT;

    companion object {
        val VALUES = entries.toTypedArray()

        fun of(ordinal: Int): PublicChatType {
            for (value in VALUES) {
                if (value.ordinal == ordinal) {
                    return value
                }
            }
            return NONE
        }
    }
}


internal fun PacketConsumer.messagePublic() {
    addListener<MessagePublic> {
        val type = it.type
        val effects: Int = it.effect
        val colors: Int = it.colour
        val message: String = it.message
        val clanType = it.clanType
        val patterns: ByteArray? = it.pattern?.asByteArray()

        val player = player

        if (player.privilege.eligibleTo(PlayerPrivilege.SUPPORT) && message.startsWith(";;")) {
            GameCommands.process(player, message.substring(2))
            return@addListener
        }

        val mute = player.muteSanction
        if (mute != null) {
            player.sendMessage("You cannot talk while the punishment is active:<br>" + mute.format() + ".")
            return@addListener
        }

        when (type) {
            2 -> {
                run {
                    val channel = player.settings.channel
                    if (channel != null) {
                        var clanMessage: String = if (message.indexOf('/') == 0) message.substring(1) else message
                        clanMessage = TextUtils.censor(clanMessage)
                        ClanManager.message(player, clanMessage)
                        val finalClanMessage = clanMessage
                        if (GameConstants.WORLD_PROFILE.isLogsDatabaseEnabled())
                            log(Level.INFO) {
                                GameLogMessage.Message.Clan(
                                    Clock.System.now(),
                                    player.dbUsername,
                                    finalClanMessage,
                                    channel.prefix,
                                    channel.owner
                                )
                            }
                        return@addListener
                    }
                }
                run {
                    if (clanType == ChatChannelType.GIM.packetIdentifier) {
                        val function = player.ironGroupMessageHandler
                        if (function != null) {
                            function.invoke(message, player.name)
                            return@addListener
                        }
                    }
                }
            }

            3 -> {
                if (clanType == ChatChannelType.GIM.packetIdentifier) {
                    val function  = player.ironGroupMessageHandler
                    if (function != null) {
                        function.invoke(message, player.name)
                        return@addListener
                    }
                }
            }
        }
        if (player.updateFlags.get(UpdateFlag.CHAT)) {
            return@addListener
        }

        player.updateFlags.flag(UpdateFlag.CHAT)
        player.chatMessage.set(message, colors, effects, type == 1, patterns)
        if (GameConstants.WORLD_PROFILE.isLogsDatabaseEnabled())
            log(Level.INFO) {
                GameLogMessage.Message.Public(
                    Clock.System.now(),
                    player.dbUsername,
                    message
                )
            }
    }
}

internal fun PacketConsumer.chatSetMode() {
    addListener<SetChatFilterSettings> {
        val player = player
        player.settings.setSetting(Setting.PUBLIC_FILTER, it.publicChatFilter)
        player.settings.setSetting(Setting.TRADE_FILTER, it.tradeChatFilter)
        player.settings.setSetting(Setting.PRIVATE_FILTER, it.privateChatFilter)
    }
}
