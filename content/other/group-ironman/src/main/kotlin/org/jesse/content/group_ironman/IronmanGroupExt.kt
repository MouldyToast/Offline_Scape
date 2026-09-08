package org.jesse.content.group_ironman

import org.jesse.content.group_ironman.player.IronmanGroupTasks
import org.jesse.content.group_ironman.player.revertHardcoreStatus
import org.jesse.game.net.packet.ChatChannelVar
import org.jesse.game.world.broadcasts.BroadcastType
import org.jesse.game.world.broadcasts.WorldBroadcasts
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.player.Player
/**
 * Execute on death of the [player], checks for HCIM deaths,
 * [adjustRemainingLives] and possible down-grade the group to regular GIM.
 */
fun IronmanGroup.onDeath(player: Player, cause: Entity?) {
    if (type == IronmanGroupType.HARDCORE) {
        WorldBroadcasts.broadcast(player, BroadcastType.GROUP_HCIM_DEATH, cause, remainingLives - 1, name)
        deductLivesAndCheck()
    }
}

fun IronmanGroup.deductLivesAndCheck() {
    adjustRemainingLives(-1)
    if (remainingLives <= 0) {
        type = IronmanGroupType.NORMAL
        activeMembers.forEach {
            it.ifOnline {
                revertHardcoreStatus()
            }
        }
        trySave()
    }
}

fun IronmanGroup.setRemainingLives(lives: Int) {
    remainingLives = lives
    channel.setVariableInt(ChatChannelVar.GIM_HC_LIVES, remainingLives)
}

fun IronmanGroup.adjustRemainingLives(lives: Int) =
    setRemainingLives(remainingLives + lives)

fun IronmanGroup.getUnlockedSpaces(): Int {
    var extraSpaces = 0
    for ((i, task) in IronmanGroupTasks.VALUES.withIndex()) {
        val bit = i % 32
        val index = i / 32
        if ((tasksCompleted[index] and (1 shl bit)) != 0) {
            extraSpaces += task.extraSpaces()
        }
    }
    return IronmanGroup.BASE_SHARED_STORAGE_SPACES + extraSpaces
}

fun IronmanGroup.checkUnlockedSpaces(player: Player) {
    var totalNewSpaces = 0
    val newTasksCompleted = tasksCompleted.clone()
    for ((i, task) in IronmanGroupTasks.VALUES.withIndex()) {
        val bit = i % 32
        val index = i / 32
        val mask = 1 shl bit
        if ((newTasksCompleted[index] and mask) == 0 && task.taskCompleted.invoke(player)) {
            newTasksCompleted[index] = newTasksCompleted[index] or mask
            totalNewSpaces += task.extraSpaces()
        }
    }

    if (totalNewSpaces > 0) {
        channel.sendMessage("${player.name} has unlocked $totalNewSpaces more group storage slots!")
    }

    tasksCompleted = newTasksCompleted
}

fun IronmanGroup.isActiveMember(username: String) =
    isActiveMember(object : org.jesse.game.world.entity.player.UsernameProvider {
        override val username: String get() = username
    })
