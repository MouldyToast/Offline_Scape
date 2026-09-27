package org.jesse.game.content.skills.sailing

import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.worldentity.WorldEntities
import org.jesse.game.world.entity.worldentity.WorldEntity

/**
 * Sailing state for one live boat. The engine [WorldEntity] is the boat's body (deck, position, angle);
 * everything here is sailing's own and lives only as long as the entity does (see [Boats]).
 */
class Boat internal constructor(
    val entity: WorldEntity,
    val type: BoatType,
) {
    /** Player navigating at the helm, or null. */
    var helmsman: Player? = null
        internal set

    /** Ticks since the helmsman took the helm (drives the 10-tick helm loop anim). */
    internal var helmTicks: Int = 0

    /** Heading the helm is steering towards, 0..15 (angle = heading * 128). */
    var targetHeading: Int = entity.angle shr 7
        internal set

    /**
     * Movement mode, mirrored to the sidepanel varbit `sailing_sidepanel_boat_move_mode` (19175):
     * 0 sails down, 1 half sails, 2 full sails, 3 reverse, 4 at the helm while idle.
     * A freshly spawned boat starts in 4 (controls capture t32: first board 0 -> 4; re-board t329 left it at 0).
     * See [Sailing] for the per-mode speed rules.
     */
    var moveMode: Int = Sailing.MODE_HELM_IDLE
        internal set

    /** Whether the sails are fully set (mode 2). */
    val sailsSet: Boolean
        get() = moveMode == Sailing.MODE_SAILS

    /** Ticks left in the current wind gust (trim window), 0 when there is no gust. */
    internal var gustTicks: Int = 0

    /** Ticks until the next gust while under full sail, -1 when not scheduled. */
    internal var nextGustIn: Int = -1

    /**
     * Trim boost progress: -1 when not boosting, 0 on the trim tick, then 1..boostDuration for the boosted ticks
     * (the boost speed applies from the tick after the trim, controls capture t291 trim -> t292..t311 at 256).
     */
    internal var boostTick: Int = -1

    /** Current speed in fine units per tick. */
    var speed: Int = 0
        internal set

    /** Ticks this boat has existed (drives the 5-tick `sailing_boat_spawned_*` varbit cadence). */
    internal var ticks: Int = 0

    /** Whether a trim boost is running. */
    val boosting: Boolean
        get() = boostTick >= 0

    /** Whether this boat is still live (its world entity has not despawned). */
    val isLive: Boolean
        get() = Boats[entity] === this
}

/**
 * Live boats, keyed by their world entity. A boat is registered when sailing spawns it ([spawn]) and dropped
 * when the engine despawns the entity ([SailingWorldEntityListener.onDespawn]). World entities that sailing
 * did not spawn have no [Boat] and are ignored by every sailing hook.
 *
 * World thread only.
 */
object Boats {
    /** [WorldEntity] has identity equality, so this is an identity map. */
    private val boats = HashMap<WorldEntity, Boat>()

    @JvmStatic
    fun spawn(type: BoatType, ownerIndex: Int, tileX: Int, tileZ: Int, level: Int, angle: Int): Boat? {
        val entity = WorldEntities.spawn(type.template, ownerIndex, tileX, tileZ, level, angle) ?: return null
        val boat = Boat(entity, type)
        boats[entity] = boat
        return boat
    }

    @JvmStatic
    operator fun get(entity: WorldEntity): Boat? = boats[entity]

    /** The boat whose deck contains [tile], or null. */
    @JvmStatic
    fun at(tile: Location): Boat? {
        val entity = WorldEntities.atTile(tile) ?: return null
        return boats[entity]
    }

    /** The first live boat owned by player [ownerIndex], or null. */
    @JvmStatic
    fun ownedBy(ownerIndex: Int): Boat? = boats.values.firstOrNull { it.entity.ownerIndex == ownerIndex }

    /** Despawns [boat]'s world entity; the engine then calls back into [SailingWorldEntityListener]. */
    @JvmStatic
    fun despawn(boat: Boat) {
        WorldEntities.despawn(boat.entity)
    }

    internal fun remove(entity: WorldEntity) {
        boats.remove(entity)
    }
}
