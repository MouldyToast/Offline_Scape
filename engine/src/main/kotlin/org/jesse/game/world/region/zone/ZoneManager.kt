package org.jesse.game.world.region.zone

import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.flooritem.FloorItem
import org.jesse.game.world.`object`.AttachedObject
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.Chunk
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import net.rsprot.protocol.api.util.ZonePartialEnclosedCacheBuffer
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.game.outgoing.util.OpFlags
import net.rsprot.protocol.game.outgoing.zone.payload.*
import net.rsprot.protocol.message.ZoneProt
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author Kris | 22/08/2024
 */
object ZoneManager {
    private val logger = LoggerFactory.getLogger(ZoneManager::class.java)
    private val zoneUpdates = Int2ObjectOpenHashMap<MutableList<ZoneProt>>(1000)
    private val dummyZoneUpdates = Int2ObjectOpenHashMap<MutableList<DummyZoneEvent>>(1000)

    fun appendUpdate(
        chunk: Chunk,
        event: ZoneProt,
    ) {
        appendUpdate(chunk.chunkId, event)
    }

    fun appendUpdate(
        chunkId: Int,
        event: ZoneProt,
    ) {
        val cur = zoneUpdates.get(chunkId)
        if (cur != null) {
            cur.add(event)
            return
        }
        val new = ArrayList<ZoneProt>(1)
        new.add(event)
        zoneUpdates.put(chunkId, new)
    }

    fun appendUpdate(
        chunk: Chunk,
        event: DummyZoneEvent,
    ) {
        appendUpdate(chunk.chunkId, event)
    }

    fun appendUpdate(
        chunkId: Int,
        event: DummyZoneEvent,
    ) {
        val cur = dummyZoneUpdates.get(chunkId)
        if (cur != null) {
            cur.add(event)
            return
        }
        val new = ArrayList<DummyZoneEvent>(1)
        new.add(event)
        dummyZoneUpdates.put(chunkId, new)
    }

    fun clear(chunk: Chunk) {
        clear(chunk.chunkId)
    }

    fun clear(chunkId: Int) {
        zoneUpdates.remove(chunkId)
        dummyZoneUpdates.remove(chunkId)
    }

    fun getUpdates(chunkId: Int): List<ZoneProt>? {
        return zoneUpdates.get(chunkId)
    }

    fun getDummyUpdates(chunkId: Int): List<DummyZoneEvent>? {
        return dummyZoneUpdates.get(chunkId)
    }

    private val trackedZonesSet: IntOpenHashSet = IntOpenHashSet()
    private val buffers: Int2ObjectOpenHashMap<EnumMap<OldSchoolClientType, ByteBuf>> = Int2ObjectOpenHashMap()
    private val zonePartialEnclosedCacheBuffer: ZonePartialEnclosedCacheBuffer = ZonePartialEnclosedCacheBuffer()

    fun computeSharedEvents() {
        try {
            val players = World.getPlayers()
            val trackedZones = trackedZonesSet
            val buffers = buffers

            for (player in players) {
                @Suppress("SENSELESS_COMPARISON")
                if (player == null || player.isNulled || !player.isAllocated ||
                    player.buildAreaManager == null || player.buildAreaManager.chunksInScope == null
                ) {
                    continue
                }
                trackedZones += player.buildAreaManager.chunksInScope
            }
            val iterator = trackedZones.intIterator()
            while (iterator.hasNext()) {
                val zoneId = iterator.nextInt()
                val updates = getUpdates(zoneId) ?: continue
                val calculatedBuffer = try {
                    zonePartialEnclosedCacheBuffer.computeZone(updates)
                } catch (e: Exception) {
                    val typesOfUpdates = HashMap<Class<*>, Int>()
                    for (update in updates) {
                        typesOfUpdates.compute(update.javaClass) { _, u ->
                            (u ?: 0) + 1
                        }
                    }
                    logger.error("Unable to calculate zone updates for $zoneId; updates: $typesOfUpdates", e)
                    continue
                }
                buffers.put(zoneId, calculatedBuffer)
            }
        } catch (e: Exception) {
            logger.error("Unable to compute shared events", e)
        }
    }

    private fun clearZoneBuffers() {
        this.trackedZonesSet.clear()
        this.buffers.clear()
    }

    fun getSharedEvents(zoneId: Int): EnumMap<OldSchoolClientType, ByteBuf>? {
        return buffers.get(zoneId)
    }

    fun postUpdate() {
        zonePartialEnclosedCacheBuffer.releaseBuffers()
        zoneUpdates.clear()
        dummyZoneUpdates.clear()
        clearZoneBuffers()
    }

    fun locAddChange(chunk: Int, obj: WorldObject) {
        appendUpdate(chunk, LocAddChange(obj.id, obj.x, obj.y, obj.type, obj.rotation, OpFlags.ALL_SHOWN))
    }

    fun locAddChange(chunk: Chunk, obj: WorldObject) {
        appendUpdate(chunk, LocAddChange(obj.id, obj.x, obj.y, obj.type, obj.rotation, OpFlags.ALL_SHOWN))
    }

    fun locDel(chunk: Chunk, obj: WorldObject) {
        appendUpdate(chunk, LocDel(obj.x, obj.y, obj.type, obj.rotation))
    }

    fun locDel(chunk: Int, obj: WorldObject) {
        appendUpdate(chunk, LocDel(obj.x, obj.y, obj.type, obj.rotation))
    }

    fun locAnim(chunkId: Int, obj: WorldObject, anim: Animation) {
        appendUpdate(chunkId, LocAnim(anim.id, obj.x, obj.y, obj.type, obj.rotation))
    }

    fun objDel(chunkId: Int, item: FloorItem) {
        appendUpdate(chunkId, ObjDel(item.id, item.amount, item.location.x, item.location.y))
    }

    fun soundArea(chunkId: Int, sound: SoundEffect, location: Location) {
        appendUpdate(chunkId, SoundArea(sound.id, sound.delay, sound.repetitions, sound.radius, 0, location.x, location.y))
    }

    fun mapAnim(chunkId: Int, graphics: Graphics, location: Location) {
        appendUpdate(chunkId, MapAnim(graphics.id, graphics.delay, graphics.height, location.x, location.y))
    }

    fun locMerge(chunkId: Int, target: Player, obj: AttachedObject) {
        appendUpdate(chunkId, LocMerge(
            target.index,
            obj.`object`.id,
            obj.`object`.x,
            obj.`object`.y,
            obj.`object`.type,
            obj.`object`.rotation,
            obj.startTime,
            obj.endTime,
            obj.minX,
            obj.minY,
            obj.maxX,
            obj.maxY
        ))
    }

    fun mapProjAnim(
        chunkId: Int,
        id: Int,
        startHeight: Int,
        endHeight: Int,
        startTime: Int,
        endTime: Int,
        angle: Int,
        progress: Int,
        sourceIndex: Int,
        targetIndex: Int,
        xInZone: Int,
        zInZone: Int,
        deltaX: Int,
        deltaZ: Int,
    ) {
        val level = chunkId shr 22
        val endX = xInZone + deltaX
        val endZ = zInZone + deltaZ
        appendUpdate(
            chunkId,
            MapProjAnimV2(
                id,
                startHeight,
                endHeight,
                startTime,
                endTime,
                angle,
                progress,
                sourceIndex,
                targetIndex,
                xInZone and 7,
                zInZone and 7,
                endX,
                endZ,
                level,
            )
        )
    }
}