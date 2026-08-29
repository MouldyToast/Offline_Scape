package com.near_reality.game.content.toa

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
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
