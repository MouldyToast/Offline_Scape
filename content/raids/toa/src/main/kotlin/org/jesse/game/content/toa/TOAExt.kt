package org.jesse.game.content.toa

import org.jesse.game.item.Item
import org.jesse.game.world.entity.attribute
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container
import org.jesse.game.world.entity.player.container.ContainerPolicy
import org.jesse.game.world.entity.player.container.impl.ContainerType
import java.util.*

var Player.pendingTOARewards by attribute("pendingTOARewards", TOARewards())
fun Player.getTOARewards(): List<Item> = this.pendingTOARewards.items.toList()
fun Player.buildRewardsContainer() : Container {
    val cont = Container(ContainerType.TOA_REWARD, ContainerPolicy.ALWAYS_STACK, 6, Optional.of(this))
    for(item in getTOARewards()) {
        sendDeveloperMessage("TOA Rewards: Reward: ${item.name} x ${item.amount}")
        cont.add(item)
    }
    return cont
}
