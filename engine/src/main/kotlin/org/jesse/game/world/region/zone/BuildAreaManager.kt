package org.jesse.game.world.region.zone

import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.Chunk
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectCollection
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap
import net.rsprot.protocol.api.util.ZonePartialEnclosedCacheBuffer
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.game.outgoing.zone.payload.ObjAdd
import net.rsprot.protocol.game.outgoing.zone.payload.ObjCount
import net.rsprot.protocol.game.outgoing.zone.payload.ObjDel
import net.rsprot.protocol.message.ZoneProt
import java.util.function.IntPredicate
import kotlin.math.max
import kotlin.math.min

class BuildAreaManager(private val player: Player) {
    private var lastChunk: Int = -1
    val chunksInScope = IntOpenHashSet(49)
    private val chunksToSkip = IntOpenHashSet(49)
    private val enclosedBuffer = ZonePartialEnclosedCacheBuffer()

    fun syncNewZones() {
        val currentHash = player.location.chunkHash
        if (chunksToSkip.isNotEmpty()) {
            chunksToSkip.clear()
        }
        if (currentHash == lastChunk) {
            return
        }
        lastChunk = currentHash
        val baseChunk = player.sceneBaseChunkId
        val baseX = baseChunk and 2047
        val baseY = baseChunk shr 11 and 2047
        val tile = player.location
        val tileX = tile.chunkX
        val tileY = tile.chunkY
        val startX =
            (max(
                (tileX - CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                baseX.toDouble()
            ) - baseX).toInt()
        val endX =
            (min(
                (tileX + CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                (baseX + SCENE_CHUNKS_DIAMETER - 1).toDouble()
            ) - baseX).toInt()
        val startY =
            (max(
                (tileY - CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                baseY.toDouble()
            ) - baseY).toInt()
        val endY =
            (min(
                (tileY + CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                (baseY + SCENE_CHUNKS_DIAMETER - 1).toDouble()
            ) - baseY).toInt()
        val baseGlobalX = baseX + startX
        val baseGlobalY = baseY + startY
        val maxGlobalX = baseX + endX
        val maxGlobalY = baseY + endY
        val chunksInScope = chunksInScope
        val level = tile.plane
        chunksInScope.removeIf(IntPredicate { chunk: Int ->
            val chunkZ = chunk shr 22
            if (chunkZ != level) {
                return@IntPredicate true
            }
            val chunkX = chunk and 2047
            val chunkY = chunk shr 11 and 2047
            chunkX < baseGlobalX || chunkX > maxGlobalX || chunkY < baseGlobalY || chunkY > maxGlobalY
        })

        for (x in startX..endX) {
            for (y in startY..endY) {
                val chunkX = baseX + x
                val chunkY = baseY + y
                val chunk = World.getChunk(Chunk.getChunkHash(chunkX, chunkY, level))
                if (!chunksInScope.add(chunk.chunkId)) {
                    continue
                }
                chunksToSkip += chunk.chunkId
                val spawnedObjects: Short2ObjectMap<WorldObject> = chunk.spawnedObjects
                val originalObjects: Short2ObjectMap<WorldObject> = chunk.originalObjects
                val floorItems = chunk.floorItems
                player.packetDispatcher.updateZoneFullFollows(x shl 3, y shl 3, level)
                if (!originalObjects.isEmpty()) {
                    val objects: ObjectCollection<WorldObject> = originalObjects.values
                    for (removedObject in objects) {
                        player.packetDispatcher.locDel(removedObject.x, removedObject.y, removedObject.type, removedObject.rotation)
                    }
                }
                if (!spawnedObjects.isEmpty()) {
                    val objects: ObjectCollection<WorldObject> = spawnedObjects.values
                    for (spawnedObject in objects) {
                        player.packetDispatcher.locAddChange(spawnedObject.id, spawnedObject.x, spawnedObject.y, spawnedObject.type, spawnedObject.rotation, 0b11111)
                    }
                }
                if (floorItems.isNotEmpty()) {
                    val objPayloads = mutableListOf<ZoneProt>()
                    for (item in floorItems) {
                        if (!item.isVisibleTo(player) || !player.isFloorItemDisplayed(item)) {
                            continue
                        }
                        val location = item.location
                        objPayloads += ObjAdd(
                            item.id, item.amount, location.x, location.y,
                            0b11111.toByte(), item.invisibleTicks,
                            item.invisibleTicks + item.visibleTicks, 0,
                            item.visibleTicks > 0
                        )
                    }
                    if (objPayloads.isNotEmpty()) {
                        sendEnclosed(objPayloads, x shl 3, y shl 3, level)
                    }
                }
            }
        }
        enclosedBuffer.releaseBuffers()
    }

    fun refreshScopedGroundItems(add: Boolean) {
        val baseChunk = player.sceneBaseChunkId
        val baseX = baseChunk and 2047
        val baseY = baseChunk shr 11 and 2047
        val tile = player.location
        val tileX = tile.chunkX
        val tileY = tile.chunkY
        val startX =
            (max(
                (tileX - CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                baseX.toDouble()
            ) - baseX).toInt()
        val endX =
            (min(
                (tileX + CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                (baseX + SCENE_CHUNKS_DIAMETER - 1).toDouble()
            ) - baseX).toInt()
        val startY =
            (max(
                (tileY - CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                baseY.toDouble()
            ) - baseY).toInt()
        val endY =
            (min(
                (tileY + CHUNK_SYNCHRONIZATION_RADIUS).toDouble(),
                (baseY + SCENE_CHUNKS_DIAMETER - 1).toDouble()
            ) - baseY).toInt()
        val level = player.location.plane
        for (x in startX..endX) {
            for (y in startY..endY) {
                val chunkX = baseX + x
                val chunkY = baseY + y
                val chunk = World.getChunk(Chunk.getChunkHash(chunkX, chunkY, level))
                val floorItems = chunk.floorItems
                if (floorItems.isNotEmpty()) {
                    val payloads = mutableListOf<ZoneProt>()
                    for (item in floorItems) {
                        if (!item.isVisibleTo(player) || !item.hasOwner() || player.isIronman && item.hasOwner() && item.isOwner(
                                player
                            )
                        ) {
                            continue
                        }
                        val location = item.location
                        if (add) {
                            payloads += ObjAdd(
                                item.id, item.amount, location.x, location.y,
                                0b11111.toByte(), item.invisibleTicks,
                                item.invisibleTicks + item.visibleTicks, 0,
                                item.visibleTicks > 0
                            )
                        } else {
                            payloads += ObjDel(item.id, item.amount, location.x, location.y)
                        }
                    }
                    if (payloads.isNotEmpty()) {
                        sendEnclosed(payloads, x shl 3, y shl 3, level)
                    }
                }
            }
        }
        enclosedBuffer.releaseBuffers()
    }

    fun updateDummyEvents() {
        val baseChunk = player.sceneBaseChunkId
        val baseX = (baseChunk and 2047) shl 3
        val baseY = (baseChunk shr 11 and 2047) shl 3
        for (zoneId in this.chunksInScope.intIterator()) {
            if (this.chunksToSkip.contains(zoneId)) {
                continue
            }
            val dummy = ZoneManager.getDummyUpdates(zoneId) ?: continue
            if (dummy.isEmpty()) {
                continue
            }
            val swX = (zoneId and 2047) shl 3
            val swY = (zoneId ushr 11 and 2047) shl 3
            val zonePlane = zoneId ushr 22
            val payloads = mutableListOf<ZoneProt>()
            for (update in dummy) {
                when (update) {
                    is ObjAddDummyEvent -> {
                        val item = update.floorItem
                        if (!item.isVisibleTo(player) || !player.isFloorItemDisplayed(item)) continue
                        val location = item.location
                        payloads += ObjAdd(
                            item.id, item.amount, location.x, location.y,
                            0b11111.toByte(), item.invisibleTicks,
                            item.invisibleTicks + item.visibleTicks, 0,
                            item.visibleTicks > 0
                        )
                    }
                    is ObjTurnPublicDummyEvent -> {
                        val item = update.floorItem
                        if (item.isReceiver(player) || !player.isFloorItemDisplayed(item) || (item.isVisibleToIronmenOnly && player.isIronman)) continue
                        val location = item.location
                        payloads += ObjAdd(
                            item.id, item.amount, location.x, location.y,
                            0b11111.toByte(), item.invisibleTicks,
                            item.invisibleTicks + item.visibleTicks, 0,
                            item.visibleTicks > 0
                        )
                    }
                    is ObjUpdateDummyEvent ->  {
                        val item = update.floorItem
                        if (!item.isVisibleTo(player) || !player.isFloorItemDisplayed(item)) continue
                        val location = item.location
                        payloads += ObjCount(item.id, update.oldQuantity, item.amount, location.x, location.y)
                    }
                }
            }
            if (payloads.isNotEmpty()) {
                sendEnclosed(payloads, swX - baseX, swY - baseY, zonePlane)
            }
        }
        enclosedBuffer.releaseBuffers()
    }

    fun updateGlobalEvents() {
        val baseChunk = player.sceneBaseChunkId
        val baseX = (baseChunk and 2047) shl 3
        val baseY = (baseChunk shr 11 and 2047) shl 3
        for (zoneId in this.chunksInScope.intIterator()) {
            if (this.chunksToSkip.contains(zoneId)) {
                continue
            }
            val globalUpdates = ZoneManager.getSharedEvents(zoneId)
            // TODO: Support other platforms
            val clientType = OldSchoolClientType.DESKTOP
            val desktopUpdates = globalUpdates?.get(clientType) ?: continue
            val swX = (zoneId and 2047) shl 3
            val swY = (zoneId ushr 11 and 2047) shl 3
            val zonePlane = zoneId ushr 22
            for (buf in globalUpdates) {
                player.packetDispatcher.updateZonePartialEnclosed(swX - baseX, swY - baseY, zonePlane, desktopUpdates)
            }
        }
    }

    private fun sendEnclosed(payloads: List<ZoneProt>, zoneX: Int, zoneY: Int, level: Int) {
        val buf = enclosedBuffer.computeZone(payloads)
        val desktop = buf[OldSchoolClientType.DESKTOP]?.retainedSlice() ?: return
        player.packetDispatcher.updateZonePartialEnclosed(zoneX, zoneY, level, desktop)
    }

    private companion object {
        private const val SCENE_CHUNKS_DIAMETER = Player.SCENE_DIAMETER shr 3
        private const val PLANE_COUNT = 4
        private const val CHUNK_SYNCHRONIZATION_RADIUS = 3
        private const val MAXIMUM_SYNCHRONIZATION_DISTANCE = CHUNK_SYNCHRONIZATION_RADIUS + 1 shl 3
    }
}
