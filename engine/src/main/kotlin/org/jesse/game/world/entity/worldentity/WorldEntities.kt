package org.jesse.game.world.entity.worldentity

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.jesse.Main
import org.jesse.game.world.World
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.dynamicregion.MapBuilder
import org.jesse.game.world.region.dynamicregion.OutOfBoundaryException
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException
import org.slf4j.LoggerFactory

/**
 * Registry of live world entities. Owns index allocation, the deck instance
 * allocation, deck locs, and the RSProt avatar lifecycle.
 *
 * Must only be used from the world thread (RSProt communication thread).
 */
object WorldEntities {
    private val logger = LoggerFactory.getLogger(WorldEntities::class.java)

    /** RSProt accepts world entity indices 1..4094 (README) / 1..4095 (alloc bound). */
    private const val MAX_INDEX = 4094

    private const val MIN_LEVEL = 0
    private const val MAX_LEVEL = 3

    private val entities = arrayOfNulls<WorldEntity>(MAX_INDEX + 1)

    /** Deck zone (zoneX | zoneZ << 11, level-independent) -> world entity whose deck owns it. */
    private val deckZones = Int2ObjectOpenHashMap<WorldEntity>()

    private fun deckZoneKey(zoneX: Int, zoneZ: Int): Int = zoneX or (zoneZ shl 11)

    @JvmStatic
    operator fun get(index: Int): WorldEntity? = if (index in 1..MAX_INDEX) entities[index] else null

    /** The world entity whose deck contains [tile], or null if [tile] is not on any deck. */
    @JvmStatic
    fun atTile(tile: Location): WorldEntity? = deckZones.get(deckZoneKey(tile.x shr 3, tile.y shr 3))

    /**
     * Level to resolve a clicked tile ([x], [z]) on. The client sends loc/ground clicks without a level;
     * the server uses the player's plane. A player on a deck is on the deck's level (1), but a root-world
     * tile they click (e.g. a dock gangplank, map level 1 but bridged to 0) lives on the scene level.
     */
    @JvmStatic
    fun interactionPlane(player: Player, x: Int, z: Int): Int {
        val deck = atTile(player.location) ?: return player.plane
        if (deckZones.get(deckZoneKey(x shr 3, z shr 3)) === deck) {
            return player.plane
        }
        return player.sceneLocation.plane
    }

    /**
     * Spawns a world entity of [type] whose pivot sits on the centre of tile ([tileX], [tileZ], [level]).
     * Allocates a dynamic area, copies the hull template into it on all levels, loads the deck
     * region(s) so the deck has collision, spawns the deck locs, and allocates the avatar.
     * @return the spawned entity, or null if no index or map space is available.
     */
    @JvmStatic
    fun spawn(
        type: WorldEntityType,
        ownerIndex: Int,
        tileX: Int,
        tileZ: Int,
        level: Int,
        angle: Int,
    ): WorldEntity? {
        val index = (1..MAX_INDEX).firstOrNull { entities[it] == null }
        if (index == null) {
            logger.warn("No free world entity index for {}", type)
            return null
        }
        val area = try {
            MapBuilder.findEmptyChunk(type.sizeX, type.sizeZ)
        } catch (e: OutOfSpaceException) {
            logger.error("No map space for world entity {}", type, e)
            return null
        }
        try {
            MapBuilder.copyAllPlanesMap(
                area,
                type.templateZoneX,
                type.templateZoneZ,
                area.chunkX,
                area.chunkY,
                type.sizeX,
                type.sizeZ,
            )
        } catch (e: OutOfBoundaryException) {
            logger.error("Failed to copy template for world entity {}", type, e)
            MapBuilder.destroy(area)
            return null
        }
        val entity = WorldEntity(
            index = index,
            type = type,
            ownerIndex = ownerIndex,
            area = area,
            level = level,
            fineX = WorldEntity.tileToFine(tileX),
            fineZ = WorldEntity.tileToFine(tileZ),
            angle = angle and 2047,
        )
        // Deck regions are never inside any player's scene (the scene follows the boat's root tile),
        // so the normal region loading never reaches them: load them explicitly for collision.
        val loadedRegions = HashSet<Int>()
        for (dx in 0 until type.sizeX) {
            for (dz in 0 until type.sizeZ) {
                val zoneX = entity.instanceZoneX + dx
                val zoneZ = entity.instanceZoneZ + dz
                val regionId = ((zoneX shr 3) shl 8) or (zoneZ shr 3)
                if (loadedRegions.add(regionId)) {
                    World.getRegion(regionId, true)
                }
                deckZones.put(deckZoneKey(zoneX, zoneZ), entity)
            }
        }
        for (loc in type.deck) {
            val tile = entity.deckTile(loc.dx, loc.dz, loc.level)
            World.spawnObject(WorldObject(loc.id, loc.shape, loc.rotation, tile.x, tile.y, tile.plane))
        }
        synchronized(Main.networkServiceLock) {
            entity.avatar = Main.networkService.worldEntityAvatarFactory.alloc(
                index = index,
                id = type.id,
                ownerIndex = ownerIndex,
                sizeX = type.sizeX,
                sizeZ = type.sizeZ,
                southWestZoneX = entity.instanceZoneX,
                southWestZoneZ = entity.instanceZoneZ,
                minLevel = MIN_LEVEL,
                maxLevel = MAX_LEVEL,
                fineX = entity.fineX,
                fineZ = entity.fineZ,
                projectedLevel = level,
                activeLevel = type.activeLevel,
                angle = entity.angle,
            )
        }
        entities[index] = entity
        return entity
    }

    /**
     * Moves every player standing on [entity]'s deck to its root tile, then releases the
     * avatar and the deck instance. Safe to call twice.
     */
    @JvmStatic
    fun despawn(entity: WorldEntity) {
        if (entities[entity.index] !== entity) {
            return
        }
        Sailing.leaveHelm(entity)
        val rootTile = entity.rootTile
        for (player in World.getPlayers()) {
            if (player != null && entity.containsDeckTile(player.location)) {
                Docking.exitBoat(player, rootTile)
            }
        }
        entities[entity.index] = null
        for (dx in 0 until entity.type.sizeX) {
            for (dz in 0 until entity.type.sizeZ) {
                deckZones.remove(deckZoneKey(entity.instanceZoneX + dx, entity.instanceZoneZ + dz))
            }
        }
        synchronized(Main.networkServiceLock) {
            val avatar = entity.avatar
            if (avatar != null) {
                Main.networkService.worldEntityAvatarFactory.release(avatar)
                entity.avatar = null
            }
        }
        MapBuilder.destroy(entity.area)
    }

    /**
     * Per-tick world entity processing (helm upkeep, turning, speed, movement). Called from the world
     * thread after player/NPC processing and before the info protocols are built, so avatar coord/angle
     * changes go out in this tick's WORLDENTITY_INFO.
     */
    @JvmStatic
    fun process() {
        for (i in 1..MAX_INDEX) {
            val entity = entities[i] ?: continue
            try {
                Sailing.tick(entity)
            } catch (e: Exception) {
                logger.error("World entity {} tick failed", i, e)
            }
        }
    }

    /** First live world entity owned by player [ownerIndex], or null. */
    @JvmStatic
    fun ownedBy(ownerIndex: Int): WorldEntity? {
        for (i in 1..MAX_INDEX) {
            val entity = entities[i] ?: continue
            if (entity.ownerIndex == ownerIndex) {
                return entity
            }
        }
        return null
    }

    /** Despawns every world entity owned by player [ownerIndex]. */
    @JvmStatic
    fun despawnOwnedBy(ownerIndex: Int) {
        for (i in 1..MAX_INDEX) {
            val entity = entities[i] ?: continue
            if (entity.ownerIndex == ownerIndex) {
                despawn(entity)
            }
        }
    }

    /**
     * Logout hook: a player saved while on a deck would log back in inside a freed instance,
     * so they are moved (immediately — the save follows in the same call) to the boat's root
     * tile first. Then every boat they own is despawned.
     */
    @JvmStatic
    fun onLogout(player: Player) {
        val entity = atTile(player.location)
        if (entity != null) {
            if (entity.helmsman === player) {
                Sailing.leaveHelm(entity)
            }
            // Land on the nearest dock rather than the boat's root tile, which is usually open sea.
            val dock = Dock.nearest(entity)
            player.forceLocation(dock?.landTile ?: entity.rootTile)
            Docking.setAboardVarbits(player, false)
        }
        despawnOwnedBy(player.index)
    }
}