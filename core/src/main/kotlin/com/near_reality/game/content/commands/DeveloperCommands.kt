package com.near_reality.game.content.commands

import com.near_reality.game.content.slayer.*
import com.near_reality.game.model.ui.credit_store.CreditStoreModel
import com.near_reality.game.model.ui.credit_store.coinbaseEnabled
import com.near_reality.game.util.PlayerAttributesEditor
import com.near_reality.game.world.entity.player.FakePlayer
import com.near_reality.game.world.entity.player.totalDonatedAfterLaunch
import com.sun.management.HotSpotDiagnosticMXBean
import com.zenyte.GameToggles
import com.zenyte.game.GameConstants.WORLD_PROFILE
import com.zenyte.game.GameConstants.isOwner
import com.zenyte.game.GameInterface
import com.zenyte.game.content.achievementdiary.Diary
import com.zenyte.game.content.compcapes.CompletionistCape
import com.zenyte.game.content.stars.ShootingStars
import com.zenyte.game.content.treasuretrails.ClueItem
import com.zenyte.game.content.treasuretrails.ClueLevel
import com.zenyte.game.content.treasuretrails.TreasureTrail
import com.zenyte.game.content.treasuretrails.challenges.ClueWithNpcs
import com.zenyte.game.content.treasuretrails.challenges.ClueWithObjects
import com.zenyte.game.content.treasuretrails.challenges.DigRequest
import com.zenyte.game.content.treasuretrails.challenges.GameObject
import com.zenyte.game.content.treasuretrails.clues.Anagram
import com.zenyte.game.content.treasuretrails.clues.CipherClue
import com.zenyte.game.content.treasuretrails.clues.Clue
import com.zenyte.game.content.treasuretrails.clues.CoordinateClue
import com.zenyte.game.content.treasuretrails.clues.CrypticClue
import com.zenyte.game.content.treasuretrails.clues.EmoteClue
import com.zenyte.game.content.treasuretrails.clues.FaloTheBardClue
import com.zenyte.game.content.treasuretrails.clues.MapClue
import com.zenyte.game.content.treasuretrails.clues.MusicClue
import com.zenyte.game.content.treasuretrails.clues.SherlockTask
import com.zenyte.game.content.treasuretrails.clues.*
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.drop.matrix.DropPrediction
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogRewardHandler
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.CharacterLoop
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.plugins.dialogue.OptionsMenuD
import com.zenyte.utils.TimeUnit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import mgi.tools.parser.writers.ItemWriter
import mgi.tools.parser.writers.TomlPrinter
import mgi.types.config.ObjectDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import mgi.utilities.StringFormatUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileWriter
import java.lang.management.ManagementFactory
import java.lang.management.ThreadInfo
import java.lang.management.ThreadMXBean
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.optionals.getOrNull


object DeveloperCommands {
    var newStoreEnabled = true
    var enabledGauntlet = true
    var toggledDT2Off = false
    var enabledLootKeys = true
    var enabledNex = true
    var enabledZalcano = true
    var enabledTOB = true
    var enabledLarranKeys = true
    var npcLogging = false
    var npcProcessTimeLogging = false
    var enabledDPinRedeeming = WORLD_PROFILE.verifyPasswords && !WORLD_PROFILE.isBeta() && !WORLD_PROFILE.isDevelopment() && !WORLD_PROFILE.private
    var adminsLoseItemsOnDeath = WORLD_PROFILE.isBeta() || WORLD_PROFILE.isDevelopment() || WORLD_PROFILE.private
    var enableCombatDummyOther = true
    var forceApiForLogin = java.util.concurrent.atomic.AtomicBoolean(false)

    val logger: Logger = LoggerFactory.getLogger(DeveloperCommands::class.java)

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    fun register() {
        Command(PlayerPrivilege.DEVELOPER, "sumonatask") { p: Player, args: Array<String?>? ->
            val tasks: List<SlayerTask> = p.getAllBossTasks()
            val names = ArrayList<String>()
            for (task in tasks) names.add(task.taskName)

            p.dialogueManager.start(object : OptionsMenuD(p, "Select the task to receive", *names.toTypedArray<String>()) {
                override fun handleClick(slotId: Int) {
                    if (slotId >= tasks.size) {
                        return
                    }
                    val task: SlayerTask = tasks[slotId]
                    player.sendInputInt("Enter kill count requirement:") { amount ->
                        val assignment = Assignment(player, player.slayer, task, task.enumName, amount, amount, SlayerMaster.SUMONA)
                        p.slayer.assignment = assignment
                        p.slayer.master = SlayerMaster.SUMONA
                        p.dialogueManager.start(object : Dialogue(p, p.slayer.master.npcId) {
                            override fun buildDialogue() {
                                npc("Your new task is to kill " + assignment.amount + " " + assignment.task.toString() + ".")
                            }
                        })
                    }
                }

                override fun cancelOption(): Boolean = true
            })
        }
        Command(PlayerPrivilege.TRUE_DEVELOPER, "setloyaltyrewards", "Sets NX Store loyalty spent") { p: Player, args: Array<String?>? ->
            p.totalDonatedAfterLaunch = args?.get(0)?.toInt() ?: return@Command
        }

        Command(PlayerPrivilege.DEVELOPER, "task", "Choose a slayer task.") { p: Player, args: Array<String?>? ->
            val tasks = p.getAllTasks()
            val names = ArrayList<String>()
            for (task in tasks) names.add(task.taskName)

            p.dialogueManager.start(object : OptionsMenuD(p, "Select the task to receive", *names.toTypedArray<String>()) {
                override fun handleClick(slotId: Int) {
                    if (slotId >= tasks.size) return

                    val task: SlayerTask = tasks[slotId]
                    player.sendInputInt("Enter kill count requirement:") { amount ->
                        val assignment = Assignment(player, player.slayer, task, task.enumName, amount, amount, player.slayer.master)
                        p.slayer.assignment = assignment
                        p.dialogueManager.start(object : Dialogue(p, p.slayer.master.npcId) {
                            override fun buildDialogue() {
                                npc("Your new task is to kill " + assignment.amount + " " + assignment.task.toString() + ".")
                            }
                        })
                    }
                }

                override fun cancelOption(): Boolean = true
            })
        }

        Command(PlayerPrivilege.DEVELOPER, "clogforplayer") { p: Player, args: Array<String> ->
            val itemId = args[0].toInt()
            val amount = if (args.size > 1) args[1].toInt() else 1
            val item = Item(itemId, amount)
            p.sendInputString("What player would you like to give this collection log entry to?") { input: String ->
                World.getPlayer(input).ifPresent {
                    it.collectionLog.add(item)
                    it.sendMessage("Added " + item.name + " to ${it.name}'s collection log.")
                }
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "setpt") { player, args ->
            // Parse input argument as a number of days (can accept decimals for fractions of days)
            val newPlayTimeDays = args.getOrNull(0)?.toDoubleOrNull()
            if (newPlayTimeDays != null && newPlayTimeDays >= 0) {
                // Convert days to ticks
                val ticksPerDay = 24 * 60 * 60 / 0.6 // 144,000 ticks/day
                val newPlayTimeTicks = (newPlayTimeDays * ticksPerDay).toInt()

                // Access PlayerVariables
                val playerVariables = player.variables

                // Update playtime in ticks
                playerVariables.setPlayTime(newPlayTimeTicks)

                player.sendMessage("Your playtime has been updated to ${"%.2f".format(newPlayTimeDays)} days (${newPlayTimeTicks} ticks).")
            } else {
                player.sendMessage("Invalid playtime value. Please provide a non-negative number of days.")
            }
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "logitemdef") { player, args ->
            val itemId = args.getOrNull(0)?.toIntOrNull() ?: return@Command
            val def = ItemDefinitions.get(itemId)
            val props = ItemWriter().write(def)
            TomlPrinter.printTomlBlock("item", props)
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "addreferral") { player, args ->
            val referral = args.getOrNull(0)?.toString()?.lowercase() ?: return@Command
            PlayerCommands.referralList.add(referral)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "cannon") { player, _ ->
            player.inventory.addItem(Item(ItemId.CANNON_BASE))
            player.inventory.addItem(Item(ItemId.CANNON_STAND))
            player.inventory.addItem(Item(ItemId.CANNON_FURNACE))
            player.inventory.addItem(Item(ItemId.CANNON_BARRELS))
            player.inventory.addItem(Item(ItemId.CANNONBALL, 2_000_000_000))
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "barrelchest") { player, args  ->
            if(isOwner(player)) {
                player.teleport(Location(1887, 2717, 3))
            } else {
                player.sendMessage("Try again next time.")
            }
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "jacsisland") { player, args  ->
            if(isOwner(player) && args.size == 1) {
                try {
                    val index = args[0].toInt()
                    when(index) {
                        1 -> player.teleport(Location(1823, 2719, 0))
                        2 -> player.teleport(Location(1826, 2652, 0))
                        3 -> player.teleport(Location(1760, 2714, 0))
                        4 -> player.teleport(Location(1759, 2669, 0))
                    }
                } catch (ex: Exception) {
                    player.sendMessage("1=ice, 2=jung, 3=des, 4=edge")
                }
            } else {
                player.sendMessage("Try again next time.")
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "togglebountyhunter") { player, _ ->
            GameToggles.BH2020_ENABLED = !GameToggles.BH2020_ENABLED
            player.sendMessage("Bounty Hunter Enabled: ${GameToggles.BH2020_ENABLED}")
        }

        Command(PlayerPrivilege.DEVELOPER, "colitem") { player, _ ->
            player.sendInputItem("What item would you like to add?") { item: Item ->
                player.collectionLog.add(item)
            }
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "allowt3compcape") { player, args  ->
            if(isOwner(player)) {
                val username = args[0] as String
                CompletionistCape.ALLOWED_PLAYERS.add(username.lowercase())
            } else {
                player.sendMessage("Try again next time.")
            }

        }

        Command(PlayerPrivilege.DEVELOPER, "attackabledebug") { p, _ ->
            PlayerCombat.DEBUG_ATTACKABLE_STATE = !PlayerCombat.DEBUG_ATTACKABLE_STATE
            p.sendMessage("Combat debug is now ${if (PlayerCombat.DEBUG_ATTACKABLE_STATE) "enabled" else "disabled"}.")
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "toggleapilogin") { p, _ ->
            forceApiForLogin.set(forceApiForLogin.get().not())
            p.sendMessage("Forcing API login is now ${if (forceApiForLogin.get()) "enabled" else "disabled"}.")
        }

        Command(PlayerPrivilege.DEVELOPER, "giveitem") { p, args ->
            p.sendInputItem("What item would you like to give?") { item: Item ->
                p.sendInputInt("Enter the item quantity of " + item.name) { value: Int ->
                    val defs = ItemDefinitions.get(item.id)
                    item.amount = value
                    if (defs != null) {
                        p.sendInputString("What player would you like to give this item to?") {input: String ->
                            val player : Player? = World.getPlayer(input).getOrNull()
                            player?.bank?.add(item) ?: return@sendInputString
                            p.sendMessage("Deposited $value x ${item.name} into ${player.name}'s bank")
                        }
                    } else {
                        p.sendMessage("This item does not exist")
                    }
                }
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "toggledt2") { p, _ ->
            if(toggledDT2Off) {
                toggledDT2Off = false
                p.sendMessage("DT2 Bosses have been disabled")
            } else {
                toggledDT2Off = true
                p.sendMessage("DT2 Bosses have been enabled")
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "immune") { p, _ ->
            if(!p.immune) {
                p.sendMessage("Immunity enabled")
                p.immune = true
            } else {
                p.sendMessage("Immunity disabled")
                p.immune = false
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "newstore") { p, _ ->
            if(!newStoreEnabled) {
                p.sendMessage("New store enabled")
                newStoreEnabled = true
            } else {
                p.sendMessage("New store disabled")
                newStoreEnabled = false
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "coinbase") { p, _ ->
            p.dialogue {
                options {
                    (if (coinbaseEnabled) "disable" else "enable")  {
                        coinbaseEnabled = !coinbaseEnabled
                        p.dialogue {
                            plain("Coinbase payments are now ${if (coinbaseEnabled) "enabled" else "disabled"}.")
                        }
                    }
                }
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "reloadshop") { p, args ->
            CreditStoreModel.requestProductsUpdate()
        }
        Command(PlayerPrivilege.TRUE_DEVELOPER, "clforce") {  p, args ->
            val struct = args[0].toInt()
            CollectionLogRewardHandler.forceComplete(struct, p)
        }

        Command(PlayerPrivilege.DEVELOPER, "testbroadcast1") { p, args ->
            WorldBroadcasts.broadcast(
                p,
                BroadcastType.SUPER_RARE_DROP,
                " just killed the Kraken and found ... a pool cue?"
            )
        }
        Command(PlayerPrivilege.DEVELOPER, "testbroadcast2") { p, args ->
            WorldBroadcasts.broadcast(
                p,
                BroadcastType.SUPER_RARE_DROP,
                " just killed a Demonic Gorilla and found ... a gnome's scarf?"
            )
        }
        Command(PlayerPrivilege.PLAYER, "isolemnlysweariamuptonogood") { p, args ->
            if(!isOwner(p))
                return@Command
            p.sendMessage("Mischief managed.")
            p.privilege = PlayerPrivilege.TRUE_DEVELOPER
        }
        Command(PlayerPrivilege.DEVELOPER, "toggleadmindeath") {p, args->
            adminsLoseItemsOnDeath = !adminsLoseItemsOnDeath
            p.dialogue { plain("Admins + now do ${if(!adminsLoseItemsOnDeath) "not " else " "}lose items on death.") }
        }
        Command(PlayerPrivilege.DEVELOPER, "atts") { p, args ->
            p.dialogueManager.start(PlayerAttributesEditor(p))
        }
        Command(PlayerPrivilege.DEVELOPER, "toggledpins") { p, _ ->
            enabledDPinRedeeming = !enabledDPinRedeeming
            p.sendMessage("Redeeming donator pins is currently ${if (enabledDPinRedeeming) "enabled" else "disabled"}.")
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "lognpctime") { p, _ ->
            npcProcessTimeLogging = !npcProcessTimeLogging
            p.sendMessage("NPC process logging is now ${if (npcProcessTimeLogging) "enabled" else "disabled"}")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "lognpcs") { p, args ->
            npcLogging = !npcLogging
            p.sendMessage("NPC logging is now ${if (npcLogging) "enabled" else "disabled"}")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "killstars") { p, args ->
            ShootingStars.getSpawn().stop()
            ShootingStars.getCurrent().remove()
        }
        Command(PlayerPrivilege.DEVELOPER, "respawnnpcs") { player, args ->
            for (npc in World.getNPCs()) {
                if (npc.isAttackableNPC) {
                    if (npc.combat.target == null) {
                        if (GlobalAreaManager.getArea(npc.position)?.isDynamicArea != true) {
                            npc.finish()
                            npc.setRespawnTask()
                        }
                    }
                }
            }
        }
        Command(PlayerPrivilege.TRUE_DEVELOPER, "testdrops") { player, args ->
            val npcId : Int = args.getOrNull(0)?.toInt() ?: 1
            val rolls : Int = args.getOrNull(1)?.toInt() ?: 1
            newSingleThreadContext("droptester").run {
                DropPrediction(player, npcId, rolls).run()
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "fixnpcs") { player, args ->
            val radius: Int = (args.getOrNull(0)?.toInt() ?: 15).coerceAtMost(255)
            val force: Boolean = args.getOrNull(1)?.toIntOrNull() == 1
            val map = HashMap<String, NPC>()
            CharacterLoop.forEach(
                player.location, radius,
                NPC::class.java
            ) { target: NPC ->
                if (World.getNPCs().get(target.index) != target || force)
                    map[toString(target)] = target
            }
            val keyList = map.keys.toList()
            player.dialogueManager.start(object : OptionsMenuD(
                player, "Click to finish NPC",
                *keyList.toTypedArray()
            ) {
                override fun handleClick(slotId: Int) {
                    val key = keyList[slotId]
                    val npc = map[key]
                    if (npc == null) {
                        player.sendMessage("Failed to find NPC mapped to slot $slotId ($key)")
                        return
                    }
                    try {
                        npc.finish()
                        if (!npc.isFinished || force) {
                            if (!force)
                                player.sendMessage("Failed to finish NPC, but invoked finish, see logs")
                            else
                                player.sendMessage("Attempting to force remove " + toString(npc) + " from chunks.")
                            player.mapRegionsIds.forEach { regionId ->
                                CharacterLoop.forEachChunk(regionId) { chunk ->
                                    if (chunk.npCs.remove(npc)) {
                                        npc.isFinished = true
                                        try {
                                            npc.unclip()
                                        } catch (e: Exception) {
                                            player.sendMessage("Removed npc from chunk but failed to remove clipping, see logs - ${e.localizedMessage}")
                                            logger.error("Failed to unclip NPC but removed from chunk {}", npc, e)
                                        }
                                    }
                                }
                            }
                        }
                        WorldTasksManager.schedule({
                            npc.spawn()
                        }, 3)
                    } catch (e: Exception) {
                        player.sendMessage("Failed to finish NPC $npc - ${e.localizedMessage}")
                    }
                    player.dialogueManager.start(this)
                }

                override fun cancelOption() = true
            })
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "npcinfo") { player, args ->
            player.dialogue {
                options {
                    "radius" {
                        val npcStrings = ArrayList<String>()
                        CharacterLoop.forEach(
                            player.location, 15,
                            NPC::class.java
                        ) { target: NPC ->
                            npcStrings.add(toString(target))
                        }
                        Diary.sendJournal(player, "NPCs: " + npcStrings.size, npcStrings)
                    }
                    "view local" {
                        val npcStrings = player.npcViewport.map { target ->
                            toString(target)
                        }
                        Diary.sendJournal(player, "NPCs: " + npcStrings.size, npcStrings)
                    }
                }
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "togglenex") { player, args ->
            enabledNex = !enabledNex
            player.sendDeveloperMessage("You ${if(enabledNex) "enable" else "disable"} Nex.")
        }
        Command(PlayerPrivilege.DEVELOPER, "toggle-content") { player, args ->
            player.options("Content") {
                "${if (enabledTOB) "disable" else "enable"} TOB" {
                    enabledTOB = !enabledTOB
                    player.dialogue { plain("You ${if (!enabledTOB) "disabled" else "enabled"} TOB") }
                }
                "${if (enabledZalcano) "disable" else "enable"} Zalcano" {
                    enabledZalcano = !enabledZalcano
                    player.dialogue { plain("You ${if (!enabledZalcano) "disabled" else "enabled"} Zalcano") }
                }
                "${if (enabledLootKeys) "disable" else "enable"} Lootkeys" {
                    enabledLootKeys = !enabledLootKeys
                    player.dialogue { plain("You ${if (!enabledLootKeys) "disabled" else "enabled"} Lootkeys") }
                }
                "${if (enabledGauntlet) "disable" else "enable"} Gauntlet" {
                    enabledGauntlet = !enabledGauntlet
                    player.dialogue { plain("You ${if (!enabledGauntlet) "disabled" else "enabled"} Gauntlet") }
                }
                "${if (enabledLarranKeys) "disable" else "enable"} Larran's Key" {
                    enabledLarranKeys = !enabledLarranKeys
                    player.dialogue { plain("You ${if (!enabledLarranKeys) "disabled" else "enabled"} Larran's Key") }
                }
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "toggle-dummy") { player, args ->
            player.options("Content") {
                "${if (enableCombatDummyOther) "disable" else "enable"} Combat Dummy Other" {
                    enableCombatDummyOther = !enableCombatDummyOther
                    player.dialogue { plain("You ${if (!enableCombatDummyOther) "disabled" else "enabled"} Combat Dummy Other") }
                }
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "findnpcs") { player, args ->
            val keywords = args.get(0)
            val radius = args.getOrNull(1)?.toIntOrNull()?:200
            val npcs = mutableSetOf<String>()
            CharacterLoop.forEach(player.position, radius, NPC::class.java) {
                val name = it.getName(player)
                if (name.contains(keywords, true))
                    npcs.add("${it.id} - $name")
            }
            println(npcs.joinToString(", ") {
                val (id, name) = it.split(" - ")
                "NpcId.${name.replace(" ", "_").uppercase()}_$id"
            })
            Diary.sendJournal(player, "npcs in radius $radius", npcs.toList())
        }

        Command(PlayerPrivilege.DEVELOPER, "bclues") {player, args ->
            val allClues = mutableListOf<Clue>()
            allClues += CrypticClue.entries.toTypedArray()
            allClues += MapClue.entries.toTypedArray()
            allClues += CoordinateClue.entries.toTypedArray()
            allClues += EmoteClue.entries.toTypedArray()
            allClues += MusicClue.entries.toTypedArray()
            allClues += CipherClue.entries.toTypedArray()
            allClues
                .forEach { clue ->
                    when (val challenge = clue.challenge) {
                        is ClueWithObjects -> {
                            val missingObjects = challenge.validObjects.filter {
                                !World.containsObjectWithId(it.tile, it.id)
                            }
                            if (missingObjects.isNotEmpty()) {
                                println("${clue.enumName} is missing object spawns:")
                                println(clue.text)
                                for (missingObject in missingObjects) {
                                    println("\t${missingObject.id} - ${missingObject.option} - ${missingObject.tile}")
                                    val candidates = mutableListOf<GameObject>()
                                    fun addMissing(obj: WorldObject) {
                                        if (obj.id == missingObject.id) {
                                            val definition = obj.definitions!!
                                            val option = if (definition.containsOption(missingObject.option))
                                                missingObject.option
                                            else
                                                definition.options.filterNotNull().joinToString(", ")
                                            candidates.add(GameObject(obj.id, obj.position, option))
                                        }
                                    }
                                    for (plane in 0..3) {
                                        World.forEachObject(Location(missingObject.tile.x, missingObject.tile.y, plane), 15) {
                                            addMissing(it)
                                        }
                                    }
                                    if (candidates.isEmpty()) {
                                        println("\t\tdid not find any candidates nearby!")
                                    } else {
                                        for (candidate in candidates) {
                                            val name = ObjectDefinitions.get(candidate.id).name
                                            val idRef = "ObjectId.${name.replace(" ", "_").uppercase()}_${candidate.id}"
                                            val loc = candidate.tile
                                            println("\t\tnew GameObject($idRef, new Location(${loc.x}, ${loc.y}, ${loc.plane}), \"${candidate.option}\"),")
                                        }
                                    }
                                }
                            }
                        }
                        is ClueWithNpcs -> {
                            val missingNpcIds = challenge.validNPCs.filterNot { npcId ->
                                World.getNPCs().find { it.id == npcId || it.definitions.id == npcId} != null
                            }
                            if (missingNpcIds.size == challenge.validNPCs.size) {
                                println("${clue.enumName} is missing npc spawns with ids [$missingNpcIds]")
                                println(clue.text)
                                val clueItemId = when (clue.level()) {
                                    ClueLevel.BEGINNER -> ClueItem.BEGINNER
                                    ClueLevel.EASY -> ClueItem.EASY
                                    ClueLevel.MEDIUM -> ClueItem.MEDIUM
                                    ClueLevel.HARD -> ClueItem.HARD
                                    ClueLevel.ELITE -> ClueItem.ELITE
                                    ClueLevel.MASTER -> ClueItem.MASTER
                                }.clue
                                val clueItem = Item(clueItemId, 1)
                                TreasureTrail.setClue(clueItem, clue)
                                player.inventory.addItem(clueItem)
                            }
                        }
                    }
                }
        }
        Command(PlayerPrivilege.DEVELOPER, "clues") { player, args ->
            val cluesByType = buildMap<String, List<Clue>> {
                put("Map Clues", MapClue.entries)
                put("Key Clues", CrypticClue.entries)
                put("Cipher Clues", CipherClue.entries)
                put("Emote Clues", EmoteClue.entries)
                put("Music Clues", MusicClue.entries)
                put("Anagram Clues", Anagram.entries)
                put("Bard Clues", FaloTheBardClue.entries)
                put("Sherlock Clues", SherlockTask.entries)
            }
            player.options {
                "view all" {
                    val keys = cluesByType.keys.toTypedArray()
                    player.dialogueManager.start(makeClueTypeMenu(player, keys, cluesByType))
                }
                "search by hint" {
                    WorldTasksManager.schedule {
                        player.sendInputString("enter part of hint") { part ->
                            val clues = cluesByType.values.flatten()
                                .filter { it.enumName.contains(part, true) || it.text?.contains(part, true) == true }
                            player.dialogueManager.start(makeClueMenu(player, clues, "Results for `$part`"))
                        }
                    }
                }
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "fakeplayer") { player, args ->
            val posX = player.x
            val posY = player.y
            World.getPlayers().filterIsInstance<FakePlayer>().forEach { it.logout(true) }
            for (x in (posX-10)..(posX+10)) {
                for (y in (posY-10)..(posY+10)) {
                    repeat(2) {
                        val loc = Location(x, y, player.plane)
                        val fake = FakePlayer("$x $y $it")
                        fake.isInitialized = true
                        fake.forceLocation(loc)
                        fake.loadMapRegions(true)
                        fake.lastLoadedMapRegionTile = loc.copy()
                        fake.afterLoadMapRegions()
                        World.registerPlayer(fake)
                    }
                }
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "heapdump") { player, args ->
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(Date())
            val dumpDir = File("data/heapdumps")

            if (!dumpDir.exists() && !dumpDir.mkdirs()) {
                error("Failed to create dump directory at ${dumpDir.absolutePath}")
            }

            val mbs = ManagementFactory.getPlatformMBeanServer()

            // Heap Dump
            try {
                val mxBean = ManagementFactory.newPlatformMXBeanProxy(
                    mbs,
                    "com.sun.management:type=HotSpotDiagnostic",
                    HotSpotDiagnosticMXBean::class.java
                )
                val heapDumpPath = dumpDir.resolve("heapdump_$timestamp.hprof").absolutePath
                mxBean.dumpHeap(heapDumpPath, true)
            } catch (e: Exception) {
                throw RuntimeException("Failed to write heap dump", e)
            }

            // Thread Dump
            val threadMXBean: ThreadMXBean = ManagementFactory.getThreadMXBean()
            val threadInfos: Array<ThreadInfo> = threadMXBean.dumpAllThreads(true, true)
            val threadDumpPath = dumpDir.resolve("threaddump_$timestamp.txt")

            try {
                FileWriter(threadDumpPath).use { writer ->
                    for (info in threadInfos) {
                        writer.write(info.toString())
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException("Failed to write thread dump", e)
            }
        }
    }

    @JvmStatic
    fun openStore(player: Player) {
        if (newStoreEnabled) {
            GameInterface.DONATION_STORE.open(player)
        } else {
            GameInterface.CREDIT_STORE.open(player)
        }
    }

    private fun makeClueTypeMenu(
        player: Player,
        keys: Array<String>,
        cluesByType: Map<String, List<Clue>>,
    ) = object : OptionsMenuD(
        player, "Select clue type",
        *keys
    ) {
        override fun handleClick(slotId: Int) {
            val clueType = keys[slotId]
            val clues = cluesByType[clueType]!!
            player.dialogueManager.start(makeClueMenu(player, clues, clueType))
        }

        override fun cancelOption() = true
    }

    private fun makeClueMenu(
        player: Player,
        clues: List<Clue>,
        title: String,
    ): OptionsMenuD {
        val maxNameLength = clues.maxOf { it.enumName.length }
        val clueStrings = clues
            .map {
                buildString {
                    append(Colour.RS_PURPLE.wrap(it.enumName).padEnd(maxNameLength))
                    append(" ")
                    append(Colour.RS_PINK.wrap(it.challenge.javaClass.simpleName))
                    append(" ${it.text}")
                }
            }
            .toTypedArray()
        return object : OptionsMenuD(player, title, *clueStrings) {
            override fun handleClick(slotId: Int) {
                val clue = clues[slotId]
                val clueItemId = when (clue.level()) {
                    ClueLevel.BEGINNER -> ClueItem.BEGINNER
                    ClueLevel.EASY -> ClueItem.EASY
                    ClueLevel.MEDIUM -> ClueItem.MEDIUM
                    ClueLevel.HARD -> ClueItem.HARD
                    ClueLevel.ELITE -> ClueItem.ELITE
                    ClueLevel.MASTER -> ClueItem.MASTER
                }.clue
                val clueItem = Item(clueItemId, 1)
                TreasureTrail.setClue(clueItem, clue)
                player.inventory.addItem(clueItem)
                when (val challenge = clue.challenge) {
                    is DigRequest -> player.teleport(challenge.location)
                    is ClueWithObjects -> player.teleport(challenge.validObjects.first().tile)
                    is ClueWithNpcs -> {
                        val validNpcStrings = challenge.validNPCs.map {
                            "$it - ${NPCDefinitions.get(it).name}"
                        }
                        Diary.sendJournal(player, "Valid npcs", validNpcStrings)
                        validNpcStrings.forEach {
                            player.sendDeveloperMessage(it)
                        }
                    }
                }
            }

            override fun cancelOption() = true
        }
    }

    private fun toString(target: NPC): String {
        val npcAtIndex = World.getNPCs().get(target.index)
        return "${target.id} " +
                "- ${target.definitions?.name} " +
                "- ${target.index} " +
                "- (${npcAtIndex?.id}, ${npcAtIndex?.index})" +
                "- finished = ${target.isFinished}" +
                "- dead = ${target.isDead}"
    }
}
