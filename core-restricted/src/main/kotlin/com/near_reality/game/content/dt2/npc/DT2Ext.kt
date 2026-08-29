package com.near_reality.game.content.dt2.npc

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.near_reality.game.content.offset
import com.near_reality.game.world.Boundary
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.ProjectileUtils
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.*
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.ForceMovement
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.LogLevel
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.*
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

var Player.deathsToAwakenedDuke by persistentAttribute("deaths_to_awakened_duke", 0)
var Player.deathsToDuke by persistentAttribute("deaths_to_duke", 0)

var Player.deathsToAwakenedVardorvis by persistentAttribute("deaths_to_awakened_vardorvis", 0)
var Player.deathsToVardorvis by persistentAttribute("deaths_to_vardorvis", 0)

var Player.deathsToAwakenedWhisperer by persistentAttribute("deaths_to_awakened_whisper", 0)
var Player.deathsToWhisperer by persistentAttribute("deaths_to_whisper", 0)

var Player.deathsToAwakenedLeviathan by persistentAttribute("deaths_to_awakened_leviathan", 0)
var Player.deathsToLeviathan by persistentAttribute("deaths_to_leviathan", 0)

fun Player.getOffsetLocationInArena(instance: VardorvisInstance): Optional<Location> {
    val boundary = Boundary(instance.getLocation(1124, 3423), instance.getLocation(1134, 3413))
    var potentialLocation = this.location.transform(Utils.random(4), Utils.random(4), 0)
    var searchFinderLimiter = 0
    while (!boundary.`in`(potentialLocation)) {
        potentialLocation = this.location.transform(Utils.random(4), Utils.random(4), 0)
        searchFinderLimiter++
        if (searchFinderLimiter == 20) {
            this.sendDeveloperMessage("Unable to find spawn loc for Vardorvis head.")
            log(LogLevel.ERROR, "Unable to find spawn loc for Vardorvis head.")
            return Optional.empty()
        }
    }
    return Optional.of(potentialLocation)
}

fun Player.getPotentialSpawnsInArena(instance: VardorvisInstance, radius: Int, count: Int): List<Location> {
    val potentialSpawns = mutableListOf<Location>()
    val boundary = Boundary(instance.getLocation(1124, 3423), instance.getLocation(1134, 3413))
    var searchFinderLimiter = 0
    while (searchFinderLimiter < radius * radius) {
        val potentialLocation = (this.location.transform(Utils.random(radius), Utils.random(radius), 0)) offset Pair(
            -(radius / 2),
            -(radius / 2)
        )
        if (boundary.`in`(potentialLocation))
            potentialSpawns.add(potentialLocation)
        searchFinderLimiter++
    }
    return potentialSpawns.shuffled().take(count)
}

var Player.entangled by attribute("dt2_entangled", false)
var Player.entangledFreedomCounter by attribute("dt2_entangledFreedomCounter", 0)


typealias Npc = NPC

fun Entity.schedule(delay: Int = 0, target: Entity? = null, unsafe: Boolean = false, action: () -> Unit) {
    val task = WorldTask { action() }
    schedule(task, delay, -1, target, unsafe)
}

fun Entity.schedule(
    delay: Int,
    period: Int,
    target: Entity? = null,
    unsafe: Boolean = false,
    action: () -> Unit
): WorldTask {
    val task = WorldTask { action() }
    schedule(task, delay, period, target, unsafe)
    WorldTasksManager.schedule(task, delay, period)
    return task
}

fun Entity.schedule(
    task: WorldTask,
    delay: Int,
    period: Int,
    target: Entity? = null,
    unsafe: Boolean = false,
): WorldTask {
    val controllingTask = object : WorldTask {
        override fun run() {
            if (!unsafe) {
                if (!isValid(target)) {
                    stop()
                    return
                }
            }

            if (!task.exists()) {
                return stop()
            }
        }

        override fun stop() {
            super.stop()

            if (!task.exists()) return
            task.stop()
        }
    }

    WorldTasksManager.schedule(controllingTask, 0, 0)
    if (period < 0) {
        WorldTasksManager.schedule(task, delay)
    } else {
        WorldTasksManager.schedule(task, delay, period)
    }
    return task
}


private fun Entity.isValid(target: Entity? = null): Boolean {
    if (this is NPC) {
        if (isDead || isFinished) return false
    } else if (this is Player) {
        if (isDead || isFinished) return false
    }

    if (target != null) {
        if (!target.isValid()) return false
        if (!target.position.withinDistance(position, 30)) {
            return false
        }
    }

    return true
}

fun Location.findNpc(id: Int, radius: Int = 15): NPC? {
    return World.findNPC(id, this, radius).getOrNull()
}

fun Location.findNpc(radius: Int = 0, predicate: (NPC.() -> Boolean)? = null): NPC? =
    World.findNPC(this, radius) {
        predicate?.invoke(it) ?: true
    }?.getOrNull()

fun Entity.findNpc(id: Int, radius: Int = 15): NPC? {
    return location.findNpc(id, radius)
}

fun Location.findNpcs(radius: Int = 15, predicate: (NPC.() -> Boolean)? = null): List<NPC> {
    val npcList = mutableListOf<NPC>()
    CharacterLoop.forEach(this, radius, NPC::class.java) {
        if (predicate?.invoke(it) ?: true) {
            npcList.add(it)
        }
    }
    return npcList
}

fun Location.findNpcs(vararg ids: Int, radius: Int = 15): List<NPC> = findNpcs(radius) {
    id in ids
}

fun Location.findObject(predicate: (WorldObject.() -> Boolean)? = null): WorldObject? {
    return World.getObjectWithPredicate(this) {
        predicate?.invoke(it) ?: true
    }
}

fun Location.spotanim(graphics: Graphics) {
    World.sendGraphics(graphics, this)
}

fun Location.spotanim(graphics: Graphics, height: Int = -1, delay: Int = -1) {
    World.sendGraphics(
        Graphics(
            graphics.id,
            if (delay == -1) graphics.delay else delay,
            if (height == -1) graphics.height else height
        ), this
    )
}

fun Player.clientscript(id: Int, vararg args: Any) {
    packetDispatcher.sendClientScript(id, *args)
}

fun Player.playSound(id: Int, delay: Int = 0, radius: Int = 15) =
    World.sendSoundEffect(this, SoundEffect(id, radius, delay))

fun Entity.playAnimation(animation: Animation) {
    this.animation = animation
}

fun Entity.playGraphics(graphics: Graphics) {
    this.graphics = graphics
}

fun Entity.playAnimation(id: Int, delay: Int = 0) {
    animation = Animation(id, delay)
}

fun Entity.playGraphics(id: Int, height: Int = 0, delay: Int = 0) {
    graphics = Graphics(id, delay, height)
}

fun Location.spotanim(id: Int, height: Int = 0, delay: Int = 0) {
    spotanim(Graphics(id, delay, height))
}

fun Location.playSound(id: Int, delay: Int, radius: Int = 15) =
    World.sendSoundEffect(this, SoundEffect(id, radius, delay))

fun Location.containsObjId(id: Int): Boolean {
    return findObject {
        this.id == id
    } != null
}

fun Location.containsObjShape(shape: Int) = findObject {
    this.type == shape
} != null

fun Location.direction(other: Location): Direction = Direction.getDirection(this, other)

fun Location.direction(other: Entity): Direction = direction(other.middleLocation)

fun Location.random(radius: Int, attempts: Int = 300, block: ((position: Location) -> Boolean)? = null): Location {
    var attempts = attempts
    while (attempts-- > 0) {
        val position = random(radius)
        if (block == null || block.invoke(position)) {
            return position
        }
    }

    return this
}


fun Location.randomSafe(
    radius: Int,
    attempts: Int = 300,
    predicate: ((candidate: Location) -> Boolean)? = null
): Location {
    var attempts = attempts
    while (attempts-- > 0) {
        val position: Location = transform(
            -radius + Utils.random(abs(radius) * 2),
            -radius + Utils.random(abs(radius) * 2), 0
        )
        if (position.isTileFree && (predicate == null || predicate.invoke(
                position
            ))
        ) {
            return position
        }
    }
    return this
}


fun Location.randomSafeNearest(radius: Int = 1, block: ((position: Location) -> Boolean)? = null): Location {
    // Start from the center, then iterate through each direction
    for (distance in 1..radius) {
        val positions = mutableListOf<Location>()
        for (direction in Direction.entries) {
            val position = transform(direction, distance)
            if (!position.isFloorFree || (block != null && !block.invoke(position))) {
                continue
            }

            positions.add(position)
        }

        if (positions.isNotEmpty()) {
            return positions.random()
        }
    }

    return this
}

fun Location.hasLineOfSightTo(location: Location): Boolean =
    !ProjectileUtils.isProjectileClipped(null, null, this, location, false)

fun Location.hasLineOfSightTo(entity: Entity): Boolean =
    !ProjectileUtils.isProjectileClipped(null, entity, this, entity.location, false)

fun Entity.hasLineOfSightTo(location: Location): Boolean =
    !ProjectileUtils.isProjectileClipped(this, null, this.location, location, false)

fun WorldObject.exists(): Boolean = World.containsObjectWithId(location, id)

fun WorldObject.remove() = World.removeObject(this)

val Location.players
    get() = CharacterLoop.find(
        this, 0,
        Player::class.java
    ) { true }

val Location.npcs
    get() = CharacterLoop.find(
        this, 0,
        NPC::class.java
    ) { true }

val Location.isFloorFree: Boolean
    get() = World.isFloorFree(this)

val Location.isTileFree: Boolean
    get() = World.isTileFree(this, 1)

fun WorldObject.spawn(): WorldObject {
    World.spawnObject(this)
    return this
}

val Location.instanceArea get() = GlobalAreaManager.getArea(this, DynamicArea::class.java) as DynamicArea?
val Entity.instanceArea get() = location.instanceArea
val WorldObject.instanceArea get() = location.instanceArea

val Direction.opposing get() = Direction.getOppositeDirection(this)

fun Location.inArea(clazz: KClass<out RegionArea>, instance: DynamicArea? = null): Boolean {
    if (instance != null) {
        return instance.get(this, static = false).inArea(clazz)
    }
    val area = GlobalAreaManager.getNullableArea(clazz.java)
    return area?.inside(this) ?: false
}

val Entity.jagAngle get() = JagAngle(direction)

private fun Entity.degAngle(other: Entity): Double {
    val otherCenter = other.middleLocation
    val center = middleLocation
    val dx: Int = otherCenter.x - center.x
    val dy: Int = otherCenter.y - center.y
    return atan2(dy.toDouble(), -dx.toDouble()) + Math.PI / 2
}

fun Entity.jagAngle(other: Entity): JagAngle {
    val angle = degAngle(other) * (1024 / Math.PI)
    return JagAngle(angle.toInt() and 0x7ff)
}

fun Entity.jagAngle(position: Location): JagAngle {
    return JagAngle(middleLocation.jagAngle(position))
}

private fun Location.degAngle(other: Entity): Double {
    val otherCenter = other.middleLocation
    val dx: Int = otherCenter.x - x
    val dy: Int = otherCenter.y - y
    return atan2(dy.toDouble(), -dx.toDouble()) + Math.PI / 2
}

fun Location.jagAngle(other: Entity): JagAngle {
    val angle: Double = degAngle(other) * (1024 / Math.PI)
    return JagAngle(angle.toInt() and 0x7ff)
}

private fun Location.degAngle(other: Location): Double {
    val dx: Int = other.x - x
    val dy: Int = other.y - y
    return atan2(dy.toDouble(), -dx.toDouble()) + Math.PI / 2
}

fun Location.jagAngle(other: Location): Int {
    val angle: Double = degAngle(other) * (1024 / Math.PI)
    return angle.toInt() and 0x7ff
}

fun Location.transformAngle(jagAngle: JagAngle, distance: Int) = transformAngle(jagAngle.toInt(), distance)

fun findAreaRandom(
    clazz: KClass<out RegionArea>,
    attempts: Int,
    instance: DynamicArea? = null,
    filter: (entity: Location) -> Boolean
): Location? {
    val api = GlobalAreaManager.getNullableArea(clazz.java) as? PolygonRegionArea ?: return null
    return api.polygons.map {
        val position = it.getRandomPosition(attempts) { candidate ->
            val position = candidate
            filter.invoke(instance?.get(position) ?: position)
        }

        instance?.get(position) ?: position
    }.randomOrNull()
}

operator fun DynamicArea?.get(location: Location, static: Boolean = location.instanceArea == null): Location {
    return get(location.x, location.y, location.plane, static)
}

operator fun DynamicArea?.get(x: Int, y: Int, z: Int = 0, static: Boolean = true): Location {
    if (this == null) return Location(x, y, z)
    return if (static) getLocation(x, y, z) else getStaticLocation(x, y, z)
}


fun Entity.exactMove(
    firstLocation: Location = location,
    firstDuration: Int = 0,
    secondLocation: Location,
    secondDuration: Int,
    direction: Int
) {
    forceMovement = ForceMovement(
        firstLocation,
        firstDuration,
        secondLocation,
        secondDuration,
        direction
    )
    setLocation(secondLocation)
}

fun Entity.exactMove(
    firstLocation: Location = location,
    firstDuration: Int = 0,
    secondLocation: Location,
    secondDuration: Int,
    direction: Direction
) {
    forceMovement = ForceMovement(
        firstLocation,
        firstDuration,
        secondLocation,
        secondDuration,
        direction.direction
    )
    setLocation(secondLocation)
}

inline fun <reified E : Enum<E>> attrEnumNullable(api: String, default: E? = null) =
    object : ReadWriteProperty<Entity, E?> {

        private val values = enumValues<E>()

        override fun getValue(thisRef: Entity, property: KProperty<*>): E? {
            return try {
                val reference = thisRef.temporaryAttributes.get(api) ?: default
                when (reference) {
                    is String? -> reference?.let { enumValueOf(it) as E } ?: default
                    is Int? -> reference?.let { values.getOrNull(it) } ?: default
                    else -> reference as E?
                }
            } catch (t: IllegalArgumentException) {
                default
            } catch (t: ClassCastException) {
                default
            }
        }

        override fun setValue(thisRef: Entity, property: KProperty<*>, value: E?) =
            thisRef.temporaryAttributes.set(api, value?.name)
    }

inline fun <reified T> attrOrElse(api: String, default: T) = object : ReadWriteProperty<Entity, T> {

    override fun getValue(thisRef: Entity, property: KProperty<*>): T =
        thisRef.temporaryAttributes.getOrDefault(api, default) as T

    override fun setValue(thisRef: Entity, property: KProperty<*>, value: T) =
        thisRef.temporaryAttributes.set(api, value ?: default)
}

inline fun <reified T> attrNullable(api: String) = object : ReadWriteProperty<Entity, T?> {

    @Suppress("ReplaceGetOrSet")
    override fun getValue(thisRef: Entity, property: KProperty<*>): T? = thisRef.temporaryAttributes.get(api) as? T?

    override fun setValue(thisRef: Entity, property: KProperty<*>, value: T?) =
        thisRef.temporaryAttributes.set(api, value)
}

fun random(range: IntRange): Int = Utils.random(range.first, range.last)

fun random(low: Int, high: Int): Int = Utils.random(low, high)

fun randomRoll(sides: Int, choose: Int): Boolean = random(1..sides) <= choose

fun randomBool(): Boolean = random(0..1) == 1


fun clientTicksToGameTicks(delay: Int): Int {
    var duration = delay / 30.0f
    if (duration - duration.toInt() > 0.5f) {
        duration++
    }
    return 0.coerceAtLeast(duration.toInt() - 1)
}

fun Hit.setGuaranteed() {
    val source = source
    if (source is Player) {
        val action = source.actionManager.action
        if (action is PlayerCombat) {
            damage = (action.getMaxHit(source, 1.0, 1.0, true) * 0.8).roundToInt()
        }
    }
}

fun Npc.walkToCardinal(target: Entity) = walkToCardinal(target.position.copy())

fun Npc.walkToCardinal(target: Location, ignorePathfinding: Boolean = false): Location {
    val direction = tornadoStep(location, target, cardinalOnly = true, ignorePathfinding = ignorePathfinding)
    val position = if (direction == null) target else location.transform(direction, 1)
    addWalkSteps(position.x, position.y, size, !ignorePathfinding)
    return position
}

fun Npc.tornadoStep(
    position: Location,
    target: Location,
    cardinalOnly: Boolean,
    ignorePathfinding: Boolean = false
): Direction? {
    val targetX: Int = target.getX()
    val targetY: Int = target.getY()
    val tornadoX: Int = position.getX()
    val tornadoY: Int = position.getY()
    val xSign = Integer.signum(targetX - tornadoX)
    val ySign = Integer.signum(targetY - tornadoY)
    val diffX = abs((tornadoX - targetX).toDouble()).toInt()
    val diffY = abs((tornadoY - targetY).toDouble()).toInt()
    if (diffX == 0 && diffY == 0) {
        return null
    }
    val candidate = if (diffY == diffX * 2 && xSign == ySign) {
        Direction.getDirection(xSign, ySign)
    } else if (diffX == diffY * 2 && xSign != ySign) {
        Direction.getDirection(xSign, ySign)
    } else if (diffY == 0 || diffX >= diffY * 2) {
        Direction.getDirection(xSign, 0)
    } else if (diffX == 0 || diffY >= diffX * 2) {
        Direction.getDirection(0, ySign)
    } else {
        Direction.getDirection(xSign, ySign)
    }

    if (candidate == null) {
        return null
    }

    if (cardinalOnly && candidate.isDiagonal) {
        val components: Array<Direction> = Direction.diagonalComponents(candidate)
        for (component in components) {
            if (ignorePathfinding || World.checkWalkStep(
                    plane, x, y,
                    component.movementDirection, size, false, false
                )
            ) {
                return component
            }
        }
        return null
    }
    return candidate
}

fun MapObject(
    id: Int,
    position: Location,
    type: Int,
    rotation: Int,
): WorldObject = WorldObject(
    id,
    type,
    rotation,
    position
)

fun secondsToTicks(seconds: Int): Int = (seconds * 1000) / 600

fun coord(x: Int, y: Int, z: Int = 0) = Location(x, y, z)


fun Entity.launchTimer(name: String, interval: Int, block: Entity.(startTime: Long) -> Boolean) {
    if (containsTimer(name)) {
        return
    }

    val startTime = WorldThread.getCurrentCycle()
    temporaryAttributes[name] = startTime

    val task = object : WorldTask {
        override fun stop() {
            super.stop()
            cancelTimer(name)
        }

        override fun run() {
            val timer = temporaryAttributes[name] as Long? ?: return stop()
            if (!block(startTime)) {
                return stop()
            }
        }
    }

    schedule(task, 0, interval - 1)
}

fun Entity.cancelTimer(name: String) {
    temporaryAttributes.remove(name)
}

fun Entity.containsTimer(name: String): Boolean {
    return temporaryAttributes.containsKey(name)
}


val Entity.occupiedArea
    get() = when (this) {
        is NPC -> OccupiedArea(position, this.size, this.size)
        else -> OccupiedArea(position, 1, 1)
    }

val Location.occupiedArea get() = OccupiedArea(this, 1, 1)


@Suppress("DuplicatedCode")
@JvmInline
value class OccupiedArea(private val packed: Long) {
    constructor(location: Location, width: Int, height: Int) :
            this((location.hashCode()).toLong() or ((width.toLong() and MAX_DIMENSION_PACKED) shl 30) or ((height.toLong() and MAX_DIMENSION_PACKED) shl 45))

    constructor(location: Location, size: Int = 1) : this(location, size, size)

    constructor(first: Location, second: Location) : this(
        Location(
            minOf(first.x, second.x),
            minOf(first.y, second.y),
            first.plane
        ),
        (maxOf(first.x, second.x) - minOf(first.x, second.x)) + 1,
        (maxOf(first.y, second.y) - minOf(first.y, second.y)) + 1
    )


    val x: Int get() = ((packed shr 14) and MAX_2D_COORD).toInt()
    val y: Int get() = (packed and MAX_2D_COORD).toInt()
    val z: Int get() = ((packed shr 28) and MAX_Z_COORD).toInt()
    val width: Int get() = ((packed shr 30) and MAX_DIMENSION_PACKED).toInt()
    val height: Int get() = ((packed shr 45) and MAX_DIMENSION_PACKED).toInt()

    val center: Location get() = Location(x + (width / 2), y + (height / 2), z)

    init {
        check(width in 1..MAX_DIMENSION) { "The width of the rectangle must be in range of 1..$MAX_DIMENSION" }
        check(height in 1..MAX_DIMENSION) { "The height of the rectangle must be in range of 1..$MAX_DIMENSION" }
    }

    private fun getAxisDistances(other: OccupiedArea): Point {
        val p1 = getComparisonPoint(other)
        val p2 = other.getComparisonPoint(this)
        return Point(abs(p1.x - p2.x), abs(p1.y - p2.y))
    }

    fun distanceTo(other: OccupiedArea): Int = if (this.z != other.z) Int.MAX_VALUE else distanceTo2D(other)

    private fun distanceTo(location: Location): Int = distanceTo(OccupiedArea(location, 1, 1))

    fun distanceTo2D(other: OccupiedArea): Int {
        val distances = getAxisDistances(other)
        return max(distances.x, distances.y)
    }

    fun distanceTo2D(location: Location): Int = distanceTo2D(OccupiedArea(location, 1, 1))

    fun isInMeleeDistance(other: OccupiedArea): Boolean {
        if (other.z != z) return false
        val distances = getAxisDistances(other)
        return distances.x + distances.y == 1
    }

    fun intersectsWith(other: OccupiedArea): Boolean {
        if (z != other.z) return false
        val distances = getAxisDistances(other)
        return distances.x + distances.y == 0
    }

    private fun getComparisonPoint(other: OccupiedArea): Point {
        val x = when {
            other.x <= this.x -> this.x
            other.x >= this.x + this.width - 1 -> this.x + this.width - 1
            else -> other.x
        }
        val y = when {
            other.y <= this.y -> this.y
            other.y >= this.y + this.height - 1 -> this.y + this.height - 1
            else -> other.y
        }
        return Point(x, y)
    }


    inline fun forEachTile(processor: (Location) -> Unit) {
        for (dx in 0 until width) {
            for (dy in 0 until height) {
                processor(Location(x + dx, y + dy, z))
            }
        }
    }

    fun getOccupiedTiles(): List<Location> {
        val list = ArrayList<Location>(width * height)
        forEachTile { list += it }
        return list
    }

    operator fun contains(position: Location): Boolean {
        if (position.x < x || position.y < y) return false
        if (position.x >= x + width || position.y >= y + height) return false
        return position.plane == z
    }

    override fun toString(): String = "OccupiedArea(x=$x, y=$y, z=$z, width=$width, height=$height)"

    fun translate(direction: Direction, steps: Int): OccupiedArea {
        val nextX = x + (direction.offsetX * steps)
        val nextY = y + (direction.offsetY * steps)
        return OccupiedArea(coord(nextX, nextY, z), width, height)
    }

    companion object {
        private const val MAX_2D_COORD = 0x3FFFL
        private const val MAX_Z_COORD = 0x3L
        private const val MAX_DIMENSION_PACKED = 0x7FFFL
        private const val MAX_DIMENSION = 0xFL
    }

    @JvmInline
    private value class Point(private val packed: Int) {
        constructor(x: Int, y: Int) : this((x and 0xFFFF) or ((y and 0xFFFF) shl 16))

        val x: Int get() = packed and 0xFFFF
        val y: Int get() = (packed shr 16) and 0xFFFF
        fun distanceTo(point: Point): Int = hypot((x - point.x).toDouble(), (y - point.y).toDouble()).toInt()
    }

    private data class MovementCollisionFlag(
        val xFlag: Int,
        val yFlag: Int,
        val xyFlag: Int,
        val xWallFlagSouth: Int,
        val xWallFlagNorth: Int,
        val yWallFlagWest: Int,
        val yWallFlagEast: Int
    )
}

/**
 * A class representing an angle in a 2048-based system. The angle is normalized to ensure
 * it is always within the range [0, 2048).
 *
 * @property angle The input angle to be normalized.
 */
@JvmInline
value class JagAngle(private val angle: Int) {

    companion object {
        const val FULL_ROTATION = 2048
        const val HALF_ROTATION = 1024
        const val QUARTER_ROTATION = 256
    }

    // Store the normalized angle, which is guaranteed to be between [0, FULL_ROTATION)
    private val normalizedAngle get() = (angle + FULL_ROTATION) % FULL_ROTATION

    /**
     * Returns the normalized angle in the range [0, FULL_ROTATION).
     *
     * @return The normalized angle.
     */
    private fun normalized(): Int = normalizedAngle

    /**
     * Calculates the shortest angular difference between this angle and a target angle.
     * The result is in the range [-1024, 1024].
     *
     * @param targetAngle The target angle to compare to.
     * @return The shortest angular difference as an integer.
     */
    fun deltaTo(targetAngle: JagAngle): Int {
        val difference = (targetAngle.normalizedAngle - this.normalizedAngle + FULL_ROTATION) % FULL_ROTATION
        return if (difference > HALF_ROTATION) difference - FULL_ROTATION else difference
    }

    /**
     * Overloaded version of [deltaTo] that supports an integer target angle.
     *
     * @param targetAngle The target angle as an integer.
     * @return The shortest angular difference as an integer.
     */
    fun deltaTo(targetAngle: Int): Int = deltaTo(JagAngle(targetAngle))

    /**
     * Adjusts this angle towards the target angle by a maximum allowed adjustment.
     *
     * @param angle The target angle to adjust towards.
     * @param maxAdjustment The maximum allowed adjustment. If null, adjusts fully to the target.
     * @return A new [JagAngle] that represents the adjusted angle.
     */
    fun setJagAngle(angle: JagAngle, maxAdjustment: Int? = null): JagAngle {
        var angleDifference = deltaTo(angle)
        if (maxAdjustment != null) {
            angleDifference = angleDifference.coerceIn(-maxAdjustment, maxAdjustment)
        }
        val newAngle = (this.normalizedAngle + angleDifference + FULL_ROTATION) % FULL_ROTATION
        return JagAngle(newAngle)
    }

    /**
     * Overloaded version of [adjustedTowards] that supports an integer target angle.
     *
     * @param targetAngle The target angle as an integer.
     * @param maxAdjustment The maximum allowed adjustment. If null, adjusts fully to the target.
     * @return A new [JagAngle] that represents the adjusted angle.
     */
    fun setJagAngle(targetAngle: Int, maxAdjustment: Int? = null): JagAngle =
        setJagAngle(JagAngle(targetAngle), maxAdjustment)

    /**
     * Checks if this angle is within a specified delta of the target angle.
     *
     * @param center The center angle to check against.
     * @param delta The allowable deviation from the center.
     * @return True if this angle is within the delta of the center, false otherwise.
     */
    fun isWithinRange(center: JagAngle, delta: Int): Boolean = deltaTo(center) in -delta..delta

    /**
     * Overloaded version of [isWithinRange] that supports an integer center angle.
     *
     * @param center The center angle as an integer.
     * @param delta The allowable deviation from the center.
     * @return True if this angle is within the delta of the center, false otherwise.
     */
    fun isWithinRange(center: Int, delta: Int): Boolean = isWithinRange(JagAngle(center), delta)

    /**
     * Rotates this angle clockwise by the given amount, defaulting to a quarter rotation (256).
     *
     * @param rotation The amount to rotate clockwise.
     * @return A new [JagAngle] that represents the rotated angle.
     */
    fun rotateClockwise(rotation: Int = QUARTER_ROTATION): JagAngle {
        return JagAngle((this.normalizedAngle + rotation + FULL_ROTATION) % FULL_ROTATION)
    }

    /**
     * Rotates this angle counterclockwise by the given amount, defaulting to a quarter rotation (256).
     *
     * @param rotation The amount to rotate counterclockwise.
     * @return A new [JagAngle] that represents the rotated angle.
     */
    fun rotateCounterClockwise(rotation: Int = QUARTER_ROTATION): JagAngle {
        return JagAngle((this.normalizedAngle - rotation + FULL_ROTATION) % FULL_ROTATION)
    }

    /**
     * Returns the inverse of this angle, which is the current angle plus a half rotation (1024).
     *
     * @return A new [JagAngle] that represents the inverse of the current angle.
     */
    fun inverse(): JagAngle {
        return JagAngle((this.normalizedAngle + HALF_ROTATION) % FULL_ROTATION)
    }

    /**
     * Converts this [JagAngle] back to its integer representation.
     *
     * @return The normalized angle as an integer.
     */
    fun toInt(): Int = normalized()

    /**
     * Translates this angle by a specified depth and returns the resulting angle.
     *
     * @param depth The amount to translate the angle by.
     * @return The translated angle as an integer.
     */
    fun translate(depth: Int): JagAngle = JagAngle((normalizedAngle + depth + FULL_ROTATION) % FULL_ROTATION)

    /**
     * Converts this [JagAngle] to an [DirectionApi] object.
     */
    fun toDirection() = Direction.fromJagAngle(normalizedAngle)

    override fun toString(): String {
        return "JagAngle[$normalizedAngle) ${toDirection()}]"
    }
}
