package com.near_reality.game.content.pvm_arena.player

import com.near_reality.game.content.pvm_arena.PvmArenaManager
import com.near_reality.game.content.pvm_arena.PvmArenaState
import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.near_reality.game.content.pvm_arena.area.PvmArenaBlueFightArea
import com.near_reality.game.content.pvm_arena.area.PvmArenaFightArea
import com.near_reality.game.content.pvm_arena.area.PvmArenaRedFightArea
import com.near_reality.game.content.pvm_arena.item.applyBandage
import com.near_reality.game.content.pvm_arena.npc.boss.PvmArenaBoss
import com.near_reality.game.world.entity.player.FakePlayer
import com.zenyte.Main
import com.zenyte.game.GameInterface
import com.zenyte.game.content.consumables.drinks.Potion
import com.zenyte.game.item.ids.*
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.actions.NPCHandler
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.CharacterLoop
import com.zenyte.game.world.region.GlobalAreaManager
import net.rsprot.protocol.game.incoming.buttons.If3Button
import net.rsprot.protocol.game.incoming.locs.OpLoc
import net.rsprot.protocol.game.incoming.misc.user.MoveGameClick
import net.rsprot.protocol.message.IncomingGameMessage
import net.rsprot.protocol.util.CombinedId

fun spawnFakePlayers(
    player: Player,
    amountPerTeam: Int,
) {
    repeat(amountPerTeam) {
        spawn(player, PvmArenaTeam.Blue, it, PvmArenaBlueFightArea.randomSpawnLocation())
        spawn(player, PvmArenaTeam.Red, it, PvmArenaRedFightArea.randomSpawnLocation())
    }
    player.sendDeveloperMessage("Spawned $amountPerTeam fake players per team")
}

private fun spawn(player: Player, team: PvmArenaTeam, index: Int, location: Location) {
    val fake = FakePlayer("$team-$index")
    fake.setDefaultSettings()
    fake.forceLocation(location.copy())
    copy(player, fake)
    World.registerPlayer(fake)
    fake.loadMapRegions(true)
    fake.lastLoadedMapRegionTile = location.copy()
    fake.afterLoadMapRegions()
    fake.putBooleanAttribute("registered", true)

    WorldTasksManager.schedule(1) {
        fake.isInitialized = true
        fake.combatDefinitions.isAutoRetaliate = false
//        fake.onLogin()
        WorldTasksManager.schedule(1) {
//            fake.forceLocation(location.copy())
            scheduleAutoPvmArenaTask(fake)
        }
    }
}

private fun copy(player: Player, target: Player) {
    if (target.getNumericAttribute("first_99_skill").toInt() == -1) {
        target.addAttribute("first_99_skill", 0)
    }
    for (i in 0..22) {
        target.getSkills().setSkill(i, 99, 13034431.0)
    }
    target.combatDefinitions.initialize(player.combatDefinitions)
    target.getInventory().setInventory(player.inventory)
    target.getEquipment().setEquipment(player.equipment)
    target.combatDefinitions.refresh()
    target.bonuses.update()
    target.updateFlags.flag(UpdateFlag.APPEARANCE)
}

fun scheduleAutoPvmArenaTask(fake: Player) {
    listOf(
        Potion.SUPER_ATTACK_POTION,
        Potion.SUPER_DEFENCE_POTION,
        Potion.SUPER_STRENGTH_POTION
    ).forEach {
        it.onConsumption(fake)
    }
    scheduleStateHandlerTask(0, 1) {
        when (val state = PvmArenaManager.state) {
            is PvmArenaState.Ended,
            is PvmArenaState.Idle -> {
                if (fake is FakePlayer)
                    fake.logout(true)
                return@scheduleStateHandlerTask false
            }

            is PvmArenaState.Open -> {
                if (fake.area == null) {
                    GlobalAreaManager.getArea(fake)?.add(fake)
                }
                fake.setForceTalk(fake.area.name())
                if (it % 10 == 0) {
                    fake.setForceTalk("Arena is open")
                }
            }

            is PvmArenaState.Started -> {
                fake.combatDefinitions.refresh()
                if (it % 2 == 0) {
                    val attacking = fake.attacking
                    val health = fake.hitpoints
                    fake.setHitpoints(10_000)
//                    if (health < 70) {
//                        val foodInInventory = Food.entries.find { fake.inventory.containsItem(it.id) }
//                        var consumed = false
//                        if (foodInInventory != null) {
//                            fake.setForceTalk("Eating food")
//                            val (slot, foodItem) = fake.inventory.container.items.entries.find {
//                                it.value.id == foodInInventory.id
//                            }?.let { it.key to it.value }!!
//                            foodInInventory.consume(fake, foodItem, slot)
//                            consumed = true
//                        } else {
//
//                        }
//                        if (!consumed) {
//                            fake.setForceTalk("No food in inventory")
//                        }
//                    }
                    val bandageInInventory = fake.inventory.container.items.entries.find {
                        it.value.id == BANDAGES_25730
                    }?.let { it.key to it.value }
                    if (bandageInInventory != null) {
                        fake.setForceTalk("Using bandage")
                        applyBandage(fake, fake, bandageInInventory.first, bandageInInventory.second)
                    }
                    if (fake.weapon == null) {
                        val wieldableWeaponWithSlot =
                            fake.inventory.container.items.filterValues { it.definitions.containsOption("Wield") }.entries.randomOrNull()
                        if (wieldableWeaponWithSlot != null) {
                            val (slot, wieldableWeapon) = wieldableWeaponWithSlot
                            val option = wieldableWeapon.definitions.getSlotForOption("Wield")
                            val event = If3Button(
                                CombinedId(GameInterface.INVENTORY_TAB.id, 0),
                                slot,
                                wieldableWeapon.id,
                                option
                            )
                            fake.schedule(event)
                        }
                    }
                    val visibleBandages =
                        World.getAllFloorItems().filter { it.id == BANDAGES_25730 && it.isVisibleTo(fake) }
                    val inventoryFull = fake.inventory.freeSlots == 0
                    if (visibleBandages.isNotEmpty()) {
                        if (inventoryFull) {
                            fake.setForceTalk("Could not pickup bandage, Inventory is full")
                        } else {
                            val targetBandage = visibleBandages.minBy { it.location.getDistance(fake.location) }
                            if (fake.location.matches(targetBandage.location)) {
                                fake.setForceTalk("Picking up bandage")
                                val optionIndex = targetBandage.definitions.groundOptions.indexOfFirst { it == "Take" }
                                val event = OpLoc(
                                    targetBandage.id,
                                    targetBandage.location.x,
                                    targetBandage.location.y,
                                    false,
                                    optionIndex + 1,
                                )
                                fake.schedule(event)
                            } else {
                                val event = MoveGameClick( targetBandage.location.x, targetBandage.location.y, 1)
                                fake.schedule(event)
                                fake.setForceTalk("Moving to bandage")
                            }
                        }
                    } else if (it % 10 == 0 || attacking == null || attacking.isDying || attacking.isDead || attacking.isFinished) {
                        val possibleTargets = CharacterLoop.find(fake.location, 50, NPC::class.java, { true })
                            .filter { (fake.area as PvmArenaFightArea).inside(it.location) }
                            .filter { it is PvmArenaBoss }
                        val possibleTarget = possibleTargets.randomOrNull()
                        if (possibleTarget != null) {
//                            fake.setForceTalk("Attacking ${possibleTarget.name}")
                            val event = MoveGameClick( possibleTarget.location.x, possibleTarget.location.y, 1)
                            fake.schedule(event)
                            val index = possibleTarget.definitions.options.indexOfFirst { it == "Attack" }
                            NPCHandler.handle(fake, possibleTarget, true, index + 1)
                        }
                    }
                }
            }

            is PvmArenaState.StartingSoon -> {
                if (it % 10 == 0) {
                    fake.setForceTalk("Arena is starting soon")
                }
            }
        }
        return@scheduleStateHandlerTask true
    }
}

private fun scheduleStateHandlerTask(
    ticksTillStart: Int = WorldTasksManager.DEFAULT_INITIAL_DELAY,
    tickCyclePeriod: Int = WorldTasksManager.NO_PERIOD_DELAY,
    taskWithCycle: (Int) -> Boolean,
) {
    var cycle = 0
    val worldTask = object : WorldTask {
        override fun run() {
            if (!taskWithCycle(cycle++))
                stop()
        }
    }
    WorldTasksManager.schedule(worldTask, ticksTillStart, tickCyclePeriod)
}

fun Player.schedule(packet: IncomingGameMessage) {
    val session = session
    val handler = Main.networkService.gameMessageConsumerRepositoryProvider.provide()
    val consumers = handler.consumers
    val globalConsumers = handler.globalConsumers

    val consumer = consumers[packet::class.java]
    checkNotNull(consumer) {
        "Consumer for packet $packet does not exist."
    }
    try {
        consumer.consume(session, packet)
    } catch (cause: Throwable) {

    }
    if (globalConsumers.isNotEmpty()) {
        for (globalConsumer in globalConsumers) {
            try {
                globalConsumer.consume(session, packet)
            } catch (cause: Throwable) {
            }
        }
    }
}
