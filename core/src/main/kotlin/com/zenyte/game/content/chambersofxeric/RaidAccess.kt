@file:JvmName("RaidAccess")

package com.zenyte.game.content.chambersofxeric

import com.zenyte.game.content.chambersofxeric.party.RaidParty
import com.zenyte.game.content.clans.ClanChannel
import com.zenyte.game.world.entity.player.Player
import java.util.Optional

/**
 * T2-d: raid-membership resolution moved verbatim off Player.getRaid
 * (settings channel -> raid party -> raid -> membership check). Same
 * Optional contract; Java callers use RaidAccess.raid(player); Kotlin
 * callers in this package need no import.
 */
fun Player.raid(): Optional<Raid> {
    if (isNulled) {
        return Optional.empty()
    }
    val channel: ClanChannel = settings.channel ?: return Optional.empty()
    val party: RaidParty = channel.raidParty ?: return Optional.empty()
    val raid: Raid = party.raid ?: return Optional.empty()
    if (!raid.players.contains(this)) {
        return Optional.empty()
    }
    return Optional.of(raid)
}
