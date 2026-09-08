package org.jesse.game.content.toa

import org.jesse.game.GameInterface
import org.jesse.game.content.tombsofamascut.encounter.RewardEncounter
import org.jesse.game.content.tombsofamascut.raid.TOARaidParty
import org.jesse.game.item.Item
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.World
import org.jesse.game.world.broadcasts.BroadcastType
import org.jesse.game.world.broadcasts.WorldBroadcasts
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class TOARewardRoomManager(val party: TOARaidParty, val encounter: RewardEncounter) {
    private val playerToAssignedChest = hashMapOf<String, Int>()
    private val chestToRewardList = hashMapOf<Int, MutableList<Item>>()
    private var purpleInProgress: Boolean = false
    lateinit var purple: TOAUniqueReward
    private val openedSarcophagusObject = WorldObject(44934, 10, 2, encounter.getLocation(purpleSarcophagusPosition))
    private val glowingLootable = WorldObject(44935, 10, 2, encounter.getLocation(lootPosition))
    private val uniqueLootable = WorldObject(55807, 10, 2, encounter.getLocation(lootPosition))

    private fun showPurpleSarcophagus() = party.players.forEach { it.varManager.sendBitInstant(purpleVarbit, 1) }

    private fun getAvailableNormalChest(): TOACommonChest {
        val chests = TOACommonChest.entries.filterNot { chestToRewardList.containsKey(it.id) }
        return chests.first()
    }

    fun assignNormalLoot(player: Player, rewards: MutableList<Item>) {
        player.sendDeveloperMessage("TOA Rewards - Assigning normal chest...")
        val nextChest = getAvailableNormalChest()
        player.varManager.sendBitInstant(nextChest.varbit, 2)
        playerToAssignedChest[player.username] = nextChest.id
        chestToRewardList[nextChest.id] = rewards
    }

    fun assignPurpleLoot(player: Player, rewards: MutableList<Item>) {
        player.sendDeveloperMessage("TOA Rewards - Assigning purple sarcophagus...")
        showPurpleSarcophagus()
        playerToAssignedChest[player.username] = openPurpleSarcophagus
        chestToRewardList[openPurpleSarcophagus] = rewards
    }

    private fun displayNormalChestOpened(chestId: Int) {
        val data = TOACommonChest.chestIdToDataMap[chestId] ?: return
        party.players.forEach { it.varManager.sendBitInstant(data.varbit, 4) }
    }

    private fun showPurpleSarcophagusOpeningVisuals() {
        sendObjectAnimation(openedSarcophagusObject, openSarcophagusAnimation)
    }

    private fun sendObjectAnimation(obj: WorldObject, animation: Int) = World.sendObjectAnimation(obj, Animation(animation))

    private fun openLoot(player: Player, chestId: Int) {
        val assignedChestId = playerToAssignedChest[player.username] ?: return
        if (assignedChestId != chestId) {
            player.sendMessage("This chest does not belong to you. Please open your own.")
            return
        }
        if(!chestToRewardList.containsKey(chestId) && player.pendingTOARewards.items.isNotEmpty()) {
            GameInterface.TOA_LOOT.open(player)
            return
        }

        val rewards = chestToRewardList[chestId] ?: return
        if (rewards.isEmpty()) {
            player.sendMessage("You have already looted your chest.")
            return
        }

        if (chestId != openPurpleSarcophagus) {
            player.animation = openChestAnimation
            displayNormalChestOpened(chestId)
        }

        player.pendingTOARewards.items = arrayListOf(*rewards.toTypedArray())
        chestToRewardList.remove(chestId)
        GameInterface.TOA_LOOT.open(player)
    }

    private fun resetAllChestBits(player: Player) {
        TOACommonChest.entries.forEach {
            player.varManager.sendBit(it.varbit, 0)
        }
        player.varManager.sendBit(purpleVarbit, 0)
    }

    private fun hasPurpleChest(player: Player): Boolean = playerToAssignedChest[player.username] == openPurpleSarcophagus


    private fun getRewards(player: Player): List<Item> {
        val playerChest = playerToAssignedChest[player.username] ?: return emptyList()
        return chestToRewardList[playerChest] ?: return emptyList()
    }

    private fun openPurpleSarcophagus(player: Player) {
        if (!hasPurpleChest(player)) {
            player.sendMessage("This sarcophagus is not yours to open.")
            return
        }

        if (purpleInProgress) return
        purpleInProgress = true
        player.faceLocation = encounter.getLocation(purpleFaceChest)
        player.lock()
        if(purple.animation == -1) {
            WorldTasksManager.schedule(1) {
                World.spawnObject(openedSarcophagusObject)
                player.animation = openChestAnimation
            }
            WorldTasksManager.schedule(2) {
                showPurpleSarcophagusOpeningVisuals()
            }
            WorldTasksManager.schedule(9) {
                World.spawnObject(glowingLootable)
            }
            WorldTasksManager.schedule(10) {
                announceRare(player, purple.item)
                player.unlock()
                purpleInProgress = false
            }
        } else {
            WorldTasksManager.schedule(1) {
                World.spawnObject(openedSarcophagusObject)
                player.animation = openChestAnimation
            }
            WorldTasksManager.schedule(2) {
                showPurpleSarcophagusOpeningVisuals()
            }
            WorldTasksManager.schedule(9) {
                World.spawnObject(uniqueLootable)
            }
            WorldTasksManager.schedule(15) {
                sendObjectAnimation(uniqueLootable, purple.animation)
            }
            WorldTasksManager.schedule(16) {
                announceRare(player, purple.item)
                player.unlock()
                purpleInProgress = false
            }
        }
    }

    private fun announceRare(player: Player, itemId: Int) {
        val reward = Item(itemId, 1)
        WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, reward, "Tombs of Amascut")
    }

    fun onLeave(player: Player) = resetAllChestBits(player)

    fun onReset() {
        World.removeObject(openedSarcophagusObject)
        World.removeObject(glowingLootable)
        World.removeObject(uniqueLootable)
    }

    fun handleObject(player: Player, obj: WorldObject): Boolean {
        return when(obj.id) {
            46220 -> openPurpleSarcophagus(player)
            openPurpleSarcophagus -> openLoot(player, openPurpleSarcophagus)
            in TOACommonChest.chestIds -> openLoot(player, obj.id)
            else -> null
        } != null
    }


    fun giveItemsToPlayerUponLeaving(player: Player) {
        val rewards = getRewards(player)
        if (rewards.isEmpty() && player.pendingTOARewards.items.isEmpty()) return

        if(player.pendingTOARewards.items.isNotEmpty()) {
            player.pendingTOARewards.items.forEach {
                player.collectionLog.add(it)
                player.tryAddInventoryThenBank(it)
            }
            player.sendMessage("You grab your rewards in a rush on your way out.")
            player.pendingTOARewards.items.clear()
            return
        }

        if (hasPurpleChest(player)) {
            announceRare(player, purple.item)
        }

        rewards.forEach {
            player.collectionLog.add(it)
            player.tryAddInventoryThenBank(it)
        }
        player.sendMessage("You grab your rewards in a rush on your way out.")
        player.pendingTOARewards.items.clear()
    }

    companion object {
        private const val purpleVarbit = 14373
        private val purpleFaceChest = Location(3680, 5141, 0)
        private val openChestAnimation = Animation.PUSH
        private val purpleSarcophagusPosition = Location(3679, 5140)
        private val lootPosition = Location(3679, 5141)
        private const val openSarcophagusAnimation = 9505
        private const val openPurpleSarcophagus = 44934
    }
}