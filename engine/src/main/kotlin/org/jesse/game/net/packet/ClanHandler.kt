package org.jesse.game.net.packet

import net.rsprot.protocol.game.incoming.clan.AffinedClanSettingsAddBannedFromChannel
import net.rsprot.protocol.game.incoming.clan.AffinedClanSettingsSetMutedFromChannel
import net.rsprot.protocol.game.incoming.clan.ClanChannelFullRequest
import net.rsprot.protocol.game.incoming.clan.ClanChannelKickUser
import net.rsprot.protocol.game.incoming.clan.ClanSettingsFullRequest

internal fun PacketConsumer.clanChannelKickUser() {
    addListener<ClanChannelKickUser> {
        val name = it.name
        val clanId = it.clanId
        val memberIndex = it.memberIndex
    }
}

internal fun PacketConsumer.clanChannelFullRequest() {
    addListener<ClanChannelFullRequest> {
        val clanId = it.clanId
    }
}

internal fun PacketConsumer.clanSettingsFullRequest() {
    addListener<ClanSettingsFullRequest> {
        val clanId = it.clanId
    }
}

internal fun PacketConsumer.affinedClanSettingsAddBannedFromChannel() {
    addListener<AffinedClanSettingsAddBannedFromChannel> {
        val name = it.name
        val clanId = it.clanId
        val memberIndex = it.memberIndex
    }
}

internal fun PacketConsumer.affinedClanSettingsSetMutedFromChannel() {
    addListener<AffinedClanSettingsSetMutedFromChannel> {
        val name = it.name
        val clanId = it.clanId
        val memberIndex = it.memberIndex
        val muted = it.muted
    }
}
