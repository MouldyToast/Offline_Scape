package org.jesse.game.world.entity.worldentity

import org.jesse.game.world.World
import org.jesse.game.world.entity.pathfinding.Flags
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Map collision for world entities (boats against land).
 *
 * The footprint is the template's bounds rectangle (`worldentity` config `boundssizex` x `boundssizez`; raft 128 x 384
 * = 1 x 3 tiles), centred on the pivot and rotated with the entity's angle. It is blocked when it overlaps (with
 * positive area - touching an edge is allowed) any root-map tile whose collision flags contain [BLOCKING_FLAGS].
 * Coastlines are a 1-2 tile band of floor-blocked terrain (map settings bit 1); open water has no flags.
 *
 * A move of ([dx], [dz]) fine units is resolved like the live server (derived from the captures
 * `sailing_straightintoland`, `sailing45tryingto` - 82/82 moving ticks exact - and `Sailing_around_controls`
 * - all but 9 ticks exact, the rest one quarter tile off while turning against a jagged shore):
 * 1. depenetrate: if the footprint already overlaps (it was turned into the shore), push it out by the smallest
 *    number of quarter tiles, trying directions away from the overlapped tiles first (45 capture t8, t12);
 * 2. try the full move;
 * 3. otherwise slide: move along z as far as possible in quarter-tile steps, then along x from the new z
 *    (45 capture t45-t55 slides along the coast, straight capture t68 stops 1.5 tiles short of the shore band).
 */
object WorldEntityCollision {
    /** Blocked floor (coast band / water edges), floor decorations and solid locs. */
    const val BLOCKING_FLAGS = Flags.FLOOR or Flags.FLOOR_DECORATION or Flags.OBJECT

    /** Positions move on the quarter-tile grid. */
    private const val STEP = 32
    private const val MAX_PUSH_STEPS = 4
    private const val EPSILON = 1e-9

    /**
     * Moves [entity] by ([dx], [dz]) fine units, colliding with the map. Also pushes the entity out of land it was
     * rotated into, even when ([dx], [dz]) is zero.
     * @return whether the entity's position changed.
     */
    @JvmStatic
    fun move(entity: WorldEntity, dx: Int, dz: Int): Boolean {
        val template = entity.template
        if (template.boundsSizeX <= 0 || template.boundsSizeZ <= 0) {
            if (dx == 0 && dz == 0) {
                return false
            }
            entity.moveTo(entity.level, entity.fineX + dx, entity.fineZ + dz, false)
            return true
        }
        val level = entity.level
        val angle = entity.angle
        var x = entity.fineX
        var z = entity.fineZ

        val overlapped = overlappedTileCentre(template, level, x, z, angle)
        if (overlapped != null) {
            val pushed = depenetrate(template, level, x, z, angle, overlapped)
            if (pushed != null) {
                x = pushed.first
                z = pushed.second
            }
        }

        if (dx != 0 || dz != 0) {
            if (!isBlocked(template, level, x + dx, z + dz, angle)) {
                x += dx
                z += dz
            } else {
                z += slide(dz) { step -> !isBlocked(template, level, x, z + step, angle) }
                x += slide(dx) { step -> !isBlocked(template, level, x + step, z, angle) }
            }
        }

        if (x == entity.fineX && z == entity.fineZ) {
            return false
        }
        entity.moveTo(level, x, z, false)
        return true
    }

    /** Whether [template]'s footprint with its pivot at fine ([fineX], [fineZ]) and [angle] overlaps a blocked tile. */
    @JvmStatic
    fun isBlocked(template: WorldEntityTemplate, level: Int, fineX: Int, fineZ: Int, angle: Int): Boolean =
        overlappedTileCentre(template, level, fineX, fineZ, angle) != null

    /** Largest step from [delta] toward 0 (quarter tiles) that [free] accepts, or 0. */
    private inline fun slide(delta: Int, free: (Int) -> Boolean): Int {
        if (delta == 0) {
            return 0
        }
        val unit = if (delta > 0) STEP else -STEP
        var step = delta
        while (step != 0) {
            if (free(step)) {
                return step
            }
            step -= unit
            if ((unit > 0 && step < 0) || (unit < 0 && step > 0)) {
                break
            }
        }
        return 0
    }

    /** Smallest axis push (1..4 quarter tiles) that frees the footprint, preferring directions away from [from]. */
    private fun depenetrate(
        template: WorldEntityTemplate, level: Int, x: Int, z: Int, angle: Int, from: DoubleArray,
    ): Pair<Int, Int>? {
        val awayX = x / 128.0 - from[0]
        val awayZ = z / 128.0 - from[1]
        val directions = DIRECTIONS.sortedByDescending { it[0] * awayX + it[1] * awayZ }
        for (k in 1..MAX_PUSH_STEPS) {
            for (direction in directions) {
                val px = x + direction[0] * STEP * k
                val pz = z + direction[1] * STEP * k
                if (!isBlocked(template, level, px, pz, angle)) {
                    return px to pz
                }
            }
        }
        return null
    }

    private val DIRECTIONS = listOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))

    /**
     * Separating-axis test of the rotated footprint against every blocked tile under its bounding box.
     * @return the mean centre (tile units) of the overlapped blocked tiles, or null when the footprint is free.
     */
    private fun overlappedTileCentre(
        template: WorldEntityTemplate, level: Int, fineX: Int, fineZ: Int, angle: Int,
    ): DoubleArray? {
        val radians = (angle and 2047) * PI / 1024.0
        val forwardX = -sin(radians)
        val forwardZ = -cos(radians)
        val sideX = forwardZ
        val sideZ = -forwardX
        val halfLength = template.boundsSizeZ / 256.0
        val halfWidth = template.boundsSizeX / 256.0
        val centreX = fineX / 128.0 + forwardX * (template.boundsOffsetZ / 128.0) + sideX * (template.boundsOffsetX / 128.0)
        val centreZ = fineZ / 128.0 + forwardZ * (template.boundsOffsetZ / 128.0) + sideZ * (template.boundsOffsetX / 128.0)
        val extentX = abs(forwardX) * halfLength + abs(sideX) * halfWidth
        val extentZ = abs(forwardZ) * halfLength + abs(sideZ) * halfWidth

        var sumX = 0.0
        var sumZ = 0.0
        var count = 0
        for (tileX in floor(centreX - extentX).toInt()..floor(centreX + extentX).toInt()) {
            if (centreX - extentX >= tileX + 1 - EPSILON || centreX + extentX <= tileX + EPSILON) {
                continue
            }
            for (tileZ in floor(centreZ - extentZ).toInt()..floor(centreZ + extentZ).toInt()) {
                if (centreZ - extentZ >= tileZ + 1 - EPSILON || centreZ + extentZ <= tileZ + EPSILON) {
                    continue
                }
                if (!isTileBlocked(level, tileX, tileZ)) {
                    continue
                }
                if (separated(centreX, centreZ, forwardX, forwardZ, halfLength, tileX, tileZ) ||
                    separated(centreX, centreZ, sideX, sideZ, halfWidth, tileX, tileZ)
                ) {
                    continue
                }
                sumX += tileX + 0.5
                sumZ += tileZ + 0.5
                count++
            }
        }
        return if (count == 0) null else doubleArrayOf(sumX / count, sumZ / count)
    }

    /** Whether the tile's projection on axis ([axisX], [axisZ]) misses the footprint's [-half, half] around its centre. */
    private fun separated(
        centreX: Double, centreZ: Double, axisX: Double, axisZ: Double, half: Double, tileX: Int, tileZ: Int,
    ): Boolean {
        val centre = centreX * axisX + centreZ * axisZ
        val p0 = tileX * axisX + tileZ * axisZ
        val p1 = (tileX + 1) * axisX + tileZ * axisZ
        val p2 = tileX * axisX + (tileZ + 1) * axisZ
        val p3 = (tileX + 1) * axisX + (tileZ + 1) * axisZ
        val low = min(min(p0, p1), min(p2, p3))
        val high = max(max(p0, p1), max(p2, p3))
        return high <= centre - half + EPSILON || low >= centre + half - EPSILON
    }

    /** Root-map tile collision; regions are loaded on demand (an unloaded region reads as -1 = every flag). */
    private fun isTileBlocked(level: Int, x: Int, z: Int): Boolean {
        var mask = World.getMask(level, x, z)
        if (mask == -1) {
            World.getRegion(((x shr 6) shl 8) or (z shr 6), true)
            mask = World.getMask(level, x, z)
        }
        return mask == -1 || (mask and BLOCKING_FLAGS) != 0
    }
}
