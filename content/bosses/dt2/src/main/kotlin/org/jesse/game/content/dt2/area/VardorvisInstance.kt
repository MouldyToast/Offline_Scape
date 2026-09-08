package org.jesse.game.content.dt2.area

import org.jesse.game.content.dt2.area.obj.VardorvisBoundaryObj
import org.jesse.game.content.dt2.npc.DT2BossDifficulty
import org.jesse.game.content.dt2.npc.deathsToAwakenedVardorvis
import org.jesse.game.content.dt2.npc.deathsToVardorvis
import org.jesse.game.content.dt2.npc.entangled
import org.jesse.game.content.dt2.npc.vardorvis.Vardorvis
import org.jesse.game.content.seq
import org.jesse.game.world.Boundary
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Colour
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.entity.player.variables.TickVariable
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.DynamicArea
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin
import org.jesse.game.world.region.area.plugins.DeathPlugin
import org.jesse.game.world.region.area.plugins.FullMovementPlugin
import org.jesse.game.world.region.dynamicregion.AllocatedArea
import org.jesse.game.world.region.dynamicregion.MapBuilder
import org.jesse.plugins.dialogue.PlainChat

/**
 * Location - 1122, 3412, 0
 * NPC -
 * GraphicsObject for Spike 2510
 * @author John J. Woloszyk / Kryeus
 */
class VardorvisInstance(
    allocatedArea: AllocatedArea,
    val player: Player,
    val difficulty: DT2BossDifficulty = DT2BossDifficulty.NORMAL,
    private val debugMechanics: Boolean = true
) : DynamicArea(allocatedArea, 137, 424),
    FullMovementPlugin,
    CannonRestrictionPlugin,
    DeathPlugin {

    private var vardorvis: Vardorvis = Vardorvis(this.getLocation(1129, 3419, 0), this)

    var killAxes: Boolean = false
    var killBleed: Boolean = false


    companion object {
        fun createInstance(p: Player, awakened: Boolean = false): VardorvisInstance {
            val area: AllocatedArea = MapBuilder.findEmptyChunk(8, 6)
            return VardorvisInstance(
                area, p, difficulty =
                    if (awakened) DT2BossDifficulty.AWAKENED
                    else DT2BossDifficulty.NORMAL
            ).apply {}
        }

        fun outside() = outsideLocation
        fun inside() = insideLocation

        private val insideLocation = Location(1128, 3416, 0)
        private val outsideLocation = Location(1116, 3428, 0)
    }

    override fun enter(player: Player) {

    }

    override fun leave(player: Player, logout: Boolean) {
        if (player.hpHud.isOpen)
            player.hpHud.close()
        player.blockIncomingHits(1)
        player.interfaceHandler.closeInterface(InterfacePosition.OVERLAY)
        player.packetDispatcher.sendClientScript(7021, 255)
        if (logout) {
            player.forceLocation(outside())
        }
    }

    override fun destroyRegion() {
        super.destroyRegion()
        vardorvis.finish()
        removeSpikes()
    }

    private fun removeSpikes() {
        for (spike in spikeObjects)
            World.removeObject(spike)
    }

    override fun name(): String = "Vardorvis Instance"

    override fun constructed() {
        vardorvis = Vardorvis(this.getLocation(1129, 3419, 0), this)
        vardorvis.radius = 0
        vardorvis.spawn()

        initSpikes()

        transferPlayer()
    }

    private fun transferPlayer() {
        val screen = FadeScreen(player) {
            player.setLocation(getLocation(inside()))
            player.faceDirection(Direction.WEST)
            player.dialogueManager
                .start(PlainChat(player, "You step over the icy rocks."))
        }
        player.dialogueManager.start(PlainChat(player, "You step over the icy rocks.", false))
        screen.fade()
        schedule({ screen.unfade() }, 2)
    }

    private var spikePositions = mutableListOf<Location>()
    private var spikeObjects = mutableListOf<WorldObject>()

    data class AxeLocationInformation(
        val headLocation: Location,
        val axeLocation: Location,
        val destination: Location,
        val direction: Direction,
        val corner: Boolean = false
    )

    private val baseAxes = listOf(
        /* NorthWest -> SouthEast */
        AxeLocationInformation(
            Location(1124, 3421, 0),
            Location(1124, 3421, 0),
            Location(1132, 3413),
            Direction.SOUTH_EAST,
            true
        ),

        /* North -> South */
        AxeLocationInformation(
            Location(1128, 3421, 0),
            Location(1128, 3421, 0),
            Location(1128, 3413),
            Direction.SOUTH
        ),

        /* NorthEast -> SouthWest (good) */
        AxeLocationInformation(
            Location(1132, 3421, 0),
            Location(1132, 3421, 0),
            Location(1124, 3413),
            Direction.SOUTH_WEST,
            true
        ),

        /* East -> West (good) */
        AxeLocationInformation(
            Location(1133, 3417, 0),
            Location(1133, 3417, 0),
            Location(1124, 3417),
            Direction.WEST
        ),

        /* SouthEast -> NorthWest (good) */
        AxeLocationInformation(
            Location(1132, 3413),
            Location(1132, 3413),
            Location(1124, 3421),
            Direction.NORTH_WEST,
            true
        ),

        /* South -> North (good) */
        AxeLocationInformation(
            Location(1128, 3413, 0),
            Location(1128, 3413, 0),
            Location(1128, 3421),
            Direction.NORTH
        ),

        /* SouthWest -> NorthEast (good) */
        AxeLocationInformation(
            Location(1124, 3413, 0),
            Location(1124, 3413, 0),
            Location(1132, 3421),
            Direction.NORTH_EAST,
            true
        ),

        /* West -> East */
        AxeLocationInformation(
            Location(1124, 3417, 0),
            Location(1124, 3417, 0),
            Location(1132, 3417),
            Direction.EAST
        )
    )

    val translatedAxes = buildMap<AxeLocationInformation, Location> {
        for (axe in baseAxes)
            put(axe, getLocation(axe.axeLocation))
    }

    fun applyAxeDamage() {
        val maxDamage =
            if (player.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
                17
            else
                35

        val applyDamage = Utils.random(maxDamage).coerceAtLeast(1)

        player.applyHit(Hit(applyDamage, HitType.TYPELESS))
        player.variables.schedule(15, TickVariable.VARDORVIS_BLEED)
    }

    private fun initSpikes() {
        spikePositions = mutableListOf()
        spikeObjects = mutableListOf()
        val spacing = 1

        val boundary = Boundary(1123, 3424, 1135, 3412)

        val x1 = boundary.minX
        val x2 = boundary.highX
        val y1 = boundary.minY
        val y2 = boundary.highY

        for (i in x1..x2 step spacing) {
            /* Min Y line from x1 to x2 */
            spikePositions.add(Location(i, y1))
            /* Min Y line from x1 to x2 */
            spikePositions.add(Location(i, y2))
        }

        for (i in y1..y2 step spacing) {
            /* Min X line from y1 to y2 */
            spikePositions.add(Location(x1, i))
            /* Max X line from y1 to y2 */
            spikePositions.add(Location(x2, i))
        }
        for (position in spikePositions) {
            val obj = VardorvisBoundaryObj(id = spikeIds.random(), tile = getLocation(position))
            spikeObjects.add(obj)
            World.spawnObject(obj)
        }
    }


    private val spikeIds = listOf(47599, 47600, 47601)

    /* TODO add wall awakened mechanic */
    override fun processMovement(player: Player, x: Int, y: Int): Boolean {
        if (!player.entangled && !player.bleedNextTick) {
            debugMechanic("Marking player for bleed damage next tick")
            player.bleedNextTick = true
        }
        return true
    }

    fun debugMechanic(msg: String) {
        if (debugMechanics)
            player.sendDeveloperMessage(msg)
    }

    fun resetEntanglement(message: Boolean) {
        player.entangled = false
        if (message)
            player.sendMessage(Colour.GREEN.wrap("You have broken free of Vardorvis's grip!"))
        player.interfaceHandler.closeInterfaces()
        vardorvis seq 10344
        vardorvis.animation = Animation(10345, 3)
        vardorvis.attack(player)
        player.unlock()
    }

    override fun sendDeath(player: Player?, source: Entity?): Boolean {
        player ?: return true
        DT2Module.getVardorvisStatistics(awakened = difficulty == DT2BossDifficulty.AWAKENED).globalDeathCount++
        if (difficulty == DT2BossDifficulty.AWAKENED)
            player.deathsToAwakenedVardorvis++
        else
            player.deathsToVardorvis++
        return false
    }

    override fun isSafe(): Boolean = false

    override fun getDeathInformation(): String? = null

    override fun getRespawnLocation(): Location? = null
}

