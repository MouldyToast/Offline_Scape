package org.jesse.game.world.region

import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity._Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.PrebuiltDynamicAreaManager.pendingAdditions
import org.jesse.game.world.region.PrebuiltDynamicAreaManager.pendingRemovals
import org.jesse.game.world.region.dynamicregion.AllocatedArea
import org.jesse.game.world.region.dynamicregion.MapBuilder
import org.jesse.game.world.region.dynamicregion.OutOfBoundaryException
import org.jesse.logger.NearRealityPrintStream
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*
import kotlin.math.abs

/**
 * An abstract dynamic area that supports one or more static regions.
 *
 * There are two constructors:
 *
 * 1. Single‑region (old behavior): pass a single region id (which encodes the SW static chunk coordinates).
 *
 * 2. Multi‑region: supply up to four region ids in order: sw, se, nw, ne. The sw value is required;
 *    if any of se, nw or ne is –1 that region is skipped.
 *
 * In the multi‑region case the allocated dynamic area is sized based on the region “width” and “height”
 * (in chunks) for a single region. For example, if the default (8×8 chunks) is used then providing sw/se
 * produces a 2×1 area (18×8 chunks) while providing sw, se, nw, ne produces a 2×2 area (18×18 chunks).
 *
 * The static region id is expected to be in the same format as before (so that the static chunk X/Y values are computed as:
 *    staticChunkX = (region >> 8) << 3
 *    staticChunkY = (region and 0xff) << 3
 * ).
 *
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
abstract class PrebuiltDynamicArea : DynamicArea {

    val uuid: UUID
    var registered = false
    val tasks: ObjectArrayList<DelayRepeatTask> = ObjectArrayList()
    val npcs: ObjectArrayList<NPC> = ObjectArrayList()

    // The four possible static regions. In multi‑region mode, sw is required.
    protected val sw: Int
    protected val se: Int
    protected val nw: Int
    protected val ne: Int

    // The dimensions (in chunks) of one “region” – by default 8×8.
    protected val regionWidth: Int
    protected val regionHeight: Int

    /**
     * Single‑region constructor. This behaves exactly as before.
     *
     * @param region the static region id (encoding the SW chunk coordinates)
     * @param allocatedArea the allocated dynamic area (defaults to a 8×8 allocation)
     * @param uuid a unique id (defaults to a new random UUID)
     */
    constructor(
        region: Int,
        allocatedArea: AllocatedArea = MapBuilder.findEmptyChunk(8, 8),
        uuid: UUID = UUID.randomUUID()
    ) : super(allocatedArea, region) {
        this.uuid = uuid
        this.sw = region
        this.se = -1
        this.nw = -1
        this.ne = -1
        this.regionWidth = 9
        this.regionHeight = 9
    }

    /**
     * Multi‑region constructor.
     *
     * The parameters are expected in the order: sw, se, nw, ne. For any region (other than sw) that you wish to skip,
     * pass –1.
     *
     * The overall allocated area will have a width of
     *    regionWidth * (if se or ne is provided then 2 else 1)
     * and a height of
     *    regionHeight * (if nw or ne is provided then 2 else 1).
     *
     * @param sw the mandatory southwest region id
     * @param se the southeast region id, or –1 to skip
     * @param nw the northwest region id, or –1 to skip
     * @param ne the northeast region id, or –1 to skip
     * @param regionWidth the width (in chunks) of one region (default 8)
     * @param regionHeight the height (in chunks) of one region (default 8)
     * @param uuid a unique id (defaults to a new random UUID)
     */
    constructor(
        sw: Int,
        se: Int = -1,
        nw: Int = -1,
        ne: Int = -1,
        regionWidth: Int = 8,
        regionHeight: Int = 8,
        uuid: UUID = UUID.randomUUID()
    ) : super(
        MapBuilder.findEmptyChunk(
            regionWidth * if (se != -1 || ne != -1) 2 else 1,
            regionHeight * if (nw != -1 || ne != -1) 2 else 1
        ),
        sw // Use sw as the base region so that staticChunkX/Y are computed from sw.
    ) {
        this.uuid = uuid
        this.sw = sw
        this.se = se
        this.nw = nw
        this.ne = ne
        this.regionWidth = regionWidth
        this.regionHeight = regionHeight
    }

    private fun isSingleRegion(): Boolean = se == -1 && nw == -1 && ne == -1

    override fun getLocation(x: Int, y: Int, height: Int): Location {
        if (isSingleRegion()) {
            return super.getLocation(x, y, height)
        }

        val southWestRegion = _Location.getRegionId((chunkX) shl 3, (chunkY) shl 3)
        val southEastRegion = _Location.getRegionId((chunkX + regionWidth) shl 3, (chunkY) shl 3)
        val northWestRegion = _Location.getRegionId((chunkX) shl 3, (chunkY + regionHeight) shl 3)
        val northEastRegion = _Location.getRegionId((chunkX + regionWidth) shl 3, (chunkY + regionHeight) shl 3)

        val staticRegionIdToDynamicRegionId = mapOf(
            sw to southWestRegion,
            se to southEastRegion,
            nw to northWestRegion,
            ne to northEastRegion
        ).filter { it.key != -1 }

        for ((staticRegion, dynamicRegion) in staticRegionIdToDynamicRegionId) {
            if (staticRegion != _Location.getRegionId(x, y)) {
                continue
            }

            val staticRegionAbsX = decodeStaticChunkX(staticRegion) shl 3
            val staticRegionAbsY = decodeStaticChunkY(staticRegion) shl 3

            val deltaBetweenLocationAndRegionAbsX = abs(staticRegionAbsX - x)
            val deltaBetweenLocationAndRegionAbsY = abs(staticRegionAbsY - y)

            val regionSouthWestAbsX = decodeStaticChunkX(dynamicRegion) shl 3
            val regionSouthWestAbsY = decodeStaticChunkY(dynamicRegion) shl 3

            val offset = (MapBuilder.PADDING shl 3) - 64
            return Location(
                regionSouthWestAbsX + deltaBetweenLocationAndRegionAbsX + offset,
                regionSouthWestAbsY + deltaBetweenLocationAndRegionAbsY + offset,
                height
            )
        }

        return super.getLocation(x, y, height)
    }

    override fun getStaticLocation(instanced: Location): Location {
        if (isSingleRegion()) {
            return super.getStaticLocation(instanced)
        }

        val offset = (MapBuilder.PADDING shl 3) - 64

        val realX = instanced.x - offset
        val realY = instanced.y - offset

        val instancedRegionId = _Location.getRegionId(realX, realY)

        val dynamicToStaticMap = mapOf(
            _Location.getRegionId((chunkX) shl 3, (chunkY) shl 3) to sw,
            _Location.getRegionId((chunkX + regionWidth) shl 3, (chunkY) shl 3) to se,
            _Location.getRegionId((chunkX) shl 3, (chunkY + regionHeight) shl 3) to nw,
            _Location.getRegionId((chunkX + regionWidth) shl 3, (chunkY + regionHeight) shl 3) to ne
        ).filterValues { it != -1 }

        val staticRegion = dynamicToStaticMap[instancedRegionId]
            ?: return super.getStaticLocation(instanced) // fallback if no match

        val regionSouthWestAbsX = decodeStaticChunkX(instancedRegionId) shl 3
        val regionSouthWestAbsY = decodeStaticChunkY(instancedRegionId) shl 3

        val rawDeltaX = realX - regionSouthWestAbsX
        val rawDeltaY = realY - regionSouthWestAbsY

        val staticRegionAbsX = decodeStaticChunkX(staticRegion) shl 3
        val staticRegionAbsY = decodeStaticChunkY(staticRegion) shl 3

        val candidates = listOf(
            Location(staticRegionAbsX + rawDeltaX, staticRegionAbsY + rawDeltaY, instanced.plane),
            Location(staticRegionAbsX + rawDeltaX, staticRegionAbsY - rawDeltaY, instanced.plane),
            Location(staticRegionAbsX - rawDeltaX, staticRegionAbsY + rawDeltaY, instanced.plane),
            Location(staticRegionAbsX - rawDeltaX, staticRegionAbsY - rawDeltaY, instanced.plane)
        )

        for (candidate in candidates) {
            if (_Location.getRegionId(candidate.x, candidate.y) == staticRegion) {
                return candidate
            }
        }

        return super.getStaticLocation(instanced)
    }


    /**
     * Registers a repeating event in the dynamic area.
     */
    fun addEvent(task: DelayRepeatTask) {
        if (!registered)
            registerInstance(this)
        pendingAdditions.add(task)
        tasks.add(task)
    }

    /**
     * Removes an event.
     */
    fun removeEvent(task: DelayRepeatTask) {
        pendingRemovals.add(task.uuid)
    }

    /**
     * Called when a player leaves the area.
     */
    override fun leave(player: Player, logout: Boolean) {
        if (this.players.isEmpty())
            PrebuiltDynamicAreaManager.deregister(this)
        player.mapInstance = null
    }

    /**
     * Finishes all running tasks and NPCs in the area.
     */
    fun finishTasks() {
        for (task in tasks)
            removeEvent(task)
        for (npc in npcs)
            npc.finish()
    }

    /**
     * Adds an NPC to the area.
     */
    fun addNpc(spawn: NPC) {
        npcs.add(spawn)
    }

    companion object {
        fun registerInstance(reference: PrebuiltDynamicArea) {
            PrebuiltDynamicAreaManager.register(reference)
        }

        /**
         * Helper to decode a region id into its static chunk X coordinate.
         */
        fun decodeStaticChunkX(region: Int): Int = (region shr 8) shl 3

        /**
         * Helper to decode a region id into its static chunk Y coordinate.
         */
        fun decodeStaticChunkY(region: Int): Int = (region and 0xff) shl 3
    }

    /**
     * Constructs (copies) the region from the static map into this dynamic area.
     *
     * In the multi‑region case, the SW static region is always copied into the bottom‑left.
     * If a SE region is defined (se != –1), its data is copied immediately to the right.
     * Likewise, if a NW region is defined (nw != –1), its data is copied above SW.
     * And if NE is defined, it is copied to the top‑right.
     */
    override fun constructRegion() {
        if (constructed) return
        GlobalAreaManager.add(this)
        try {
            MapBuilder.copyAllPlanesMap(
                area,
                decodeStaticChunkX(sw),
                decodeStaticChunkY(sw),
                chunkX,
                chunkY,
                regionWidth,
                regionHeight
            )
            if (se != -1) {
                MapBuilder.copyAllPlanesMap(
                    area,
                    decodeStaticChunkX(se),
                    decodeStaticChunkY(se),
                    chunkX + regionWidth,
                    chunkY,
                    regionWidth,
                    regionHeight
                )
            }
            if (nw != -1) {
                MapBuilder.copyAllPlanesMap(
                    area,
                    decodeStaticChunkX(nw),
                    decodeStaticChunkY(nw),
                    chunkX,
                    chunkY + regionHeight,
                    regionWidth,
                    regionHeight
                )
            }
            if (ne != -1) {
                MapBuilder.copyAllPlanesMap(
                    area,
                    decodeStaticChunkX(ne),
                    decodeStaticChunkY(ne),
                    chunkX + regionWidth,
                    chunkY + regionHeight,
                    regionWidth,
                    regionHeight
                )
            }
        } catch (e: OutOfBoundaryException) {
            e.printStackTrace(NearRealityPrintStream.getErrorStream())
        }
        constructed = true
        constructed()
    }
}
