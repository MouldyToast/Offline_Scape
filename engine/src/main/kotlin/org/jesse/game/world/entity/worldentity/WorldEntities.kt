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
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Registry of live world entities. Owns index allocation, the deck instance
 * allocation, deck locs, the RSProt avatar lifecycle, and the generic consequences
 * of a deck going away (evacuating passengers). Behaviour is content's, via [WorldEntityListener].
 *
 * Must only be used from the world thread (RSProt communication thread),
 * except [addListener], which is safe from any thread.
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

    /** Registered at boot (ServerLaunchEvent) while the world thread is already running - hence copy-on-write. */
    private val listeners = CopyOnWriteArrayList<WorldEntityListener>()

    private fun deckZoneKey(zoneX: Int, zoneZ: Int): Int = zoneX or (zoneZ shl 11)

    @JvmStatic
    fun addListener(listener: WorldEntityListener) {
        listeners.addIfAbsent(listener)
    }

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
     * Spawns a world entity built from [template] whose pivot sits on the centre of tile ([tileX], [tileZ], [level]).
     * Allocates a dynamic area, copies the template into it on all levels, loads the deck
     * region(s) so the deck has collision, spawns the deck locs, and allocates the avatar.
     * @return the spawned entity, or null if no index or map space is available.
     */
    @JvmStatic
    fun spawn(
        template: WorldEntityTemplate,
        ownerIndex: Int,
        tileX: Int,
        tileZ: Int,
        level: Int,
        angle: Int,
    ): WorldEntity? {
        val index = (1..MAX_INDEX).firstOrNull { entities[it] == null }
        if (index == null) {
            logger.warn("No free world entity index for {}", template)
            return null
        }
        val area = try {
            MapBuilder.findEmptyChunk(template.sizeX, template.sizeZ)
        } catch (e: OutOfSpaceException) {
            logger.error("No map space for world entity {}", template, e)
            return null
        }
        try {
            MapBuilder.copyAllPlanesMap(
                area,
                template.templateZoneX,
                template.templateZoneZ,
                area.chunkX,
                area.chunkY,
                template.sizeX,
                template.sizeZ,
            )
        } catch (e: OutOfBoundaryException) {
            logger.error("Failed to copy template for world entity {}", template, e)
            MapBuilder.destroy(area)
            return null
        }
        val entity = WorldEntity(
            index = index,
            template = template,
            ownerIndex = ownerIndex,
            area = area,
            level = level,
            fineX = WorldEntity.tileToFine(tileX),
            fineZ = WorldEntity.tileToFine(tileZ),
            angle = angle and 2047,
        )
        // Deck regions are never inside any player's scene (the scene follows the entity's root tile),
        // so the normal region loading never reaches them: load them explicitly for collision.
        val loadedRegions = HashSet<Int>()
        for (dx in 0 until template.sizeX) {
            for (dz in 0 until template.sizeZ) {
                val zoneX = entity.instanceZoneX + dx
                val zoneZ = entity.instanceZoneZ + dz
                val regionId = ((zoneX shr 3) shl 8) or (zoneZ shr 3)
                if (loadedRegions.add(regionId)) {
                    World.getRegion(regionId, true)
                }
                deckZones.put(deckZoneKey(zoneX, zoneZ), entity)
            }
        }
        for (loc in template.deck) {
            val tile = entity.deckTile(loc.dx, loc.dz, loc.level)
            World.spawnObject(WorldObject(loc.id, loc.shape, loc.rotation, tile.x, tile.y, tile.plane))
        }
        synchronized(Main.networkServiceLock) {
            entity.avatar = Main.networkService.worldEntityAvatarFactory.alloc(
                index = index,
                id = template.configId,
                ownerIndex = ownerIndex,
                sizeX = template.sizeX,
                sizeZ = template.sizeZ,
                southWestZoneX = entity.instanceZoneX,
                southWestZoneZ = entity.instanceZoneZ,
                minLevel = MIN_LEVEL,
                maxLevel = MAX_LEVEL,
                fineX = entity.fineX,
                fineZ = entity.fineZ,
                projectedLevel = level,
                activeLevel = template.activeLevel,
                angle = entity.angle,
            )
        }
        entities[index] = entity
        return entity
    }

    /**
     * Despawns [entity]: every player standing on its deck is evacuated (see [evacuate]), then
     * [WorldEntityListener.onDespawn], then the avatar and the deck instance are released. Safe to call twice.
     */
    @JvmStatic
    fun despawn(entity: WorldEntity) {
        if (entities[entity.index] !== entity) {
            return
        }
        for (player in World.getPlayers()) {
            if (player != null && entity.containsDeckTile(player.location)) {
                evacuate(player, entity, false)
            }
        }
        notifyListeners("despawn", entity) { it.onDespawn(entity) }
        entities[entity.index] = null
        for (dx in 0 until entity.template.sizeX) {
            for (dz in 0 until entity.template.sizeZ) {
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
     * Per-tick world entity processing: [WorldEntityListener.onTick] for every live entity. Called from the world
     * thread after player/NPC processing and before the info protocols are built, so avatar coord/angle
     * changes go out in this tick's WORLDENTITY_INFO.
     */
    @JvmStatic
    fun process() {
        for (i in 1..MAX_INDEX) {
            val entity = entities[i] ?: continue
            notifyListeners("tick", entity) { it.onTick(entity) }
        }
    }

    /** SET_HEADING from the client: forwarded to listeners when [player] stands on a deck. */
    @JvmStatic
    fun onSetHeading(player: Player, heading: Int) {
        val entity = atTile(player.location) ?: return
        notifyListeners("set heading", entity) { it.onSetHeading(player, entity, heading) }
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
     * Logout hook, called from `World.unregisterPlayer` before the save: a player saved while on a deck would
     * log back in inside a freed instance, so they are evacuated first (immediately - [Player.forceLocation]).
     * Then every entity they own is despawned.
     */
    @JvmStatic
    fun onLogout(player: Player) {
        val entity = atTile(player.location)
        if (entity != null) {
            evacuate(player, entity, true)
        }
        despawnOwnedBy(player.index)
    }

    /**
     * Takes [player] off [entity]'s deck: listeners get [WorldEntityListener.onEvacuate] while the player is still
     * aboard, then the player is moved to the first listener-supplied [WorldEntityListener.evacuationTile]
     * (fallback: the root tile) - immediately on [logout] (the save follows), otherwise as a normal teleport.
     */
    private fun evacuate(player: Player, entity: WorldEntity, logout: Boolean) {
        var tile: Location? = null
        for (listener in listeners) {
            tile = try {
                listener.evacuationTile(player, entity, logout)
            } catch (e: Exception) {
                logger.error("World entity {} evacuation tile failed", entity.index, e)
                null
            }
            if (tile != null) {
                break
            }
        }
        val destination = tile ?: entity.rootTile
        notifyListeners("evacuate", entity) { it.onEvacuate(player, entity, logout) }
        if (logout) {
            player.forceLocation(destination)
        } else {
            player.setLocation(destination)
        }
    }

    /** Listener failures are logged and never abort the engine's own bookkeeping. */
    private inline fun notifyListeners(what: String, entity: WorldEntity, call: (WorldEntityListener) -> Unit) {
        for (listener in listeners) {
            try {
                call(listener)
            } catch (e: Exception) {
                logger.error("World entity {} {} hook failed", entity.index, what, e)
            }
        }
    }
}