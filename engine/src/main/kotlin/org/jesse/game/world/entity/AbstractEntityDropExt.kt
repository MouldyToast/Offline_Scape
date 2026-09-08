package org.jesse.game.world.entity

import org.jesse.game.world.entity.AbstractEntity
import org.jesse.game.world.entity.ReceivedDamage
import org.jesse.game.world.entity.player.privilege.GameMode
import it.unimi.dsi.fastutil.objects.ObjectArrayList

fun AbstractEntity.sortedReceivedDamage(): List<MutableMap.MutableEntry<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>>> {
    return receivedDamage.entries.sortedByDescending { it.value.sumOf { pair -> pair.damage } }
}