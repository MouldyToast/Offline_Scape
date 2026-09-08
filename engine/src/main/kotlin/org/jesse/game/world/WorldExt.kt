package org.jesse.game.world

import org.jesse.game.item.Item
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player

fun spawnFloorItem(
    item: Item,
    owner: Player? = null,
    receiver: Player? = null,
    tile: Location? = owner?.location?:(receiver?.location)?.let { Location(it) },
    maxStack: Int = -1,
    invisibleTicks: Int = -1,
    visibleTicks: Int = -1,
    visibleToIronmenOnly: Boolean = false,
    visibleToIronmen: Boolean = false,
) = World.spawnFloorItem(
    item,
    requireNotNull(tile) { "Must specify am explicit location or owner, receiver" },
    maxStack,
    owner,
    receiver,
    invisibleTicks,
    visibleTicks,
    visibleToIronmenOnly,
    visibleToIronmen
)
