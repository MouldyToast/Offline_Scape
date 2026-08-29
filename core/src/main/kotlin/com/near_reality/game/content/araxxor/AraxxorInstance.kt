package com.near_reality.game.content.araxxor

import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.araxxor.araxytes.AraxytePattern
import com.near_reality.game.content.araxxor.attacks.AcidPool
import com.near_reality.game.content.offset
import com.near_reality.game.content.scoreboard.ScoreboardModule
import com.near_reality.game.content.scoreboard.deathsToAraxxor
import com.near_reality.game.content.seq
import com.zenyte.game.GameConstants.WORLD_PROFILE
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Position
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId.ARAXXOR
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.region.PrebuiltDynamicArea
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin
import com.zenyte.game.world.region.area.plugins.DeathPlugin
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import com.zenyte.plugins.dialogue.PlainChat
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
data class AraxxorInstance(
    val player: Player?
) : PrebuiltDynamicArea(allocatedArea = MapBuilder.findEmptyChunk(16, 16), region = 14489),
    CannonRestrictionPlugin, DeathPlugin {

    override fun name(): String = "Araxxor Instance"

    val centerArenaTile: Location = getLocation(3633, 9816, 0)
    private val entranceLocation: Location = getLocation(3647, 9815, 0)

    var araxxor: Araxxor? = null
    var hasLootAvailable = false

    val poolObjects = mutableListOf<AcidPool>()
    val araxytes = mutableListOf<Araxyte>()

    private val debugAcidPools = true

    override fun constructed() {
        transferPlayer()
    }

    override fun leave(player: Player, logout: Boolean) {
        if(player.hpHud.isOpen)
            player.hpHud.close()
        player.blockIncomingHits(1)
        player.interfaceHandler.closeInterface(InterfacePosition.OVERLAY)
        araxxor?.resetInstance()
        araxxor?.remove()
        super.leave(player, logout)
    }

    override fun isMultiwayArea(position: Position?): Boolean = true

    fun spawnAcidPool(location: Location) {
        araxxor ?: return
        /* This is the map filler object, and should not be removed */
        if(World.getObjectWithId(location, 42600) != null) return
        if(debugAcidPools) araxxor!!.debug("AraxxorInstance: Spawning Acid Pool at $location")
        player ?: return
        if (araxxor!!.isDying || araxxor!!.isDead || araxxor!!.isFinished || player.mapInstance !is AraxxorInstance) {
            if(debugAcidPools) araxxor!!.debug("AraxxorInstance: Failed to spawn Acid Pool at $location due to araxxor or instance")
            return
        }
        if (!this.getArea().inside(location)) {
            if(debugAcidPools) araxxor!!.debug("AraxxorInstance: Failed to spawn Acid Pool due to spawn loc outside of instance")
            return
        }

        val pool = AcidPool(location)
        poolObjects.add(pool)
        World.spawnObject(pool)
        araxxor!!.debug("AraxxorInstance: Spawn pool object completed at $location")
    }

    fun spawnAraxxorAndEggs() {
        player ?: return
        if (player.mapInstance != this@AraxxorInstance) return
        val assignment = player.slayer.assignment
        if(!player.isDeveloper && !WORLD_PROFILE.isBeta()) {
            if (assignment == null) {
                player.dialogue { plain("You need to be on a task to fight this boss. He will no longer respawn.") }
                return
            }
            val task = assignment.task
            if (task == null) {
                player.dialogue { plain("You need to be on a task to fight this boss. He will no longer respawn.") }
                return
            }
            val monsters = listOf(task.monsters)[0]
            if (!monsters.contains("araxxor")) {
                player.dialogue { plain("You need to be on a task to fight this boss. He will no longer respawn.") }
                return
            }
        }
        AraxytePattern(this)
            .getRandomPattern().forEach {
                it.spawn()
                it.radius = 0
                it.aggressionDistance = 96
                it.maxHitpoints = 65
                it.hitpoints = it.maxHitpoints
                araxytes.add(it)
            }
        araxxor = Araxxor(this, getLocation(3631, 9812, 0))
        araxxor?.primaryAraxyte = araxytes[0]
        araxxor?.setTransformationPreservingStats(ARAXXOR)
        araxxor?.enraged = false
        araxxor?.toxins?.reset()
        araxxor?.spawn()
        araxxor?.lock()
        araxxor?.seq(11482)
        schedule(2) {
            if (player.mapInstance != this@AraxxorInstance) return@schedule
            araxxor?.maxHitpoints?.let { player.hpHud.open(ARAXXOR, it) }
            araxxor?.faceEntity(player)
            araxxor?.setTarget(player)
            araxxor?.unlock()
            player.unlock()
            player.bossTimer.startTracking("Araxxor")
        }
    }

    private fun transferPlayer() {
        player ?: return
        player.mapInstance = this@AraxxorInstance
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(entranceLocation)
            player.faceDirection(Direction.WEST)
            player.dialogueManager.start(PlainChat(player, "You crawl through the webbed tunnel."))
        }
        player.dialogueManager.start(PlainChat(player, "You crawl through the webbed tunnel.", false))
        screen.fade()
        schedule(2) {
            screen.unfade()
            schedule(4) { spawnAraxxorAndEggs() }
        }
    }

    override fun enter(player: Player) {
        player.teleport(entranceLocation)
        player.faceEntity(araxxor)
    }

    fun getAcidSplatterLocation(location: Location?): Location {
        val xOffset = Random.nextInt(-3, 3)
        val yOffset = Random.nextInt(-3, 3)
        val tempLocation = Location(location?.offset(Pair(xOffset, yOffset)))
        return tempLocation
    }


    override fun sendDeath(player: Player?, source: Entity?): Boolean {
        player ?: return false
        ScoreboardModule.araxxorStatistics.globalDeathCount++
        player.deathsToAraxxor++
        return false
    }

    override fun isSafe(): Boolean = false

    override fun getDeathInformation(): String? = null

    override fun getRespawnLocation(): Location? = null
}
