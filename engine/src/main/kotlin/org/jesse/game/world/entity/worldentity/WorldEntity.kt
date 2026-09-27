package org.jesse.game.world.entity.worldentity

import net.rsprot.protocol.game.outgoing.info.worldentityinfo.WorldEntityAvatar
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.dynamicregion.AllocatedArea

/**
 * A world entity: an instanced piece of map (the deck) rendered by the client at a
 * fine coordinate + angle in the root world.
 *
 * Coordinates are RSProt "fine" coordinates: 128 per tile, pivot at the tile centre
 * (`tile * 128 + 64`), matching the live capture's `.5` world entity coords.
 *
 * A player is "on" a world entity when their real coordinate lies inside its deck area
 * (the same rule RSProt uses) — there is no separate boarded flag.
 */
class WorldEntity internal constructor(
    val index: Int,
    val type: WorldEntityType,
    val ownerIndex: Int,
    val area: AllocatedArea,
    level: Int,
    fineX: Int,
    fineZ: Int,
    angle: Int,
) {
    /** Level the world entity is projected onto in the root world. */
    var level: Int = level
        private set
    var fineX: Int = fineX
        private set
    var fineZ: Int = fineZ
        private set
    /** 0 = south, 512 = west, 1024 = north, 1536 = east. */
    var angle: Int = angle
        private set

    internal var avatar: WorldEntityAvatar? = null

    // ---- Sailing state (P4) ----

    /** Player navigating at the helm, or null. */
    var helmsman: Player? = null
        internal set

    /** Ticks since the helmsman took the helm (drives the 10-tick helm loop anim). */
    internal var helmTicks: Int = 0

    /** Heading the helm is steering towards, 0..15 (angle = heading * 128). */
    var targetHeading: Int = angle shr 7
        internal set

    /**
     * Movement mode, mirrored to the sidepanel varbit `sailing_sidepanel_boat_move_mode` (19175):
     * 0 idle / sails lowered, 1 slowing to a stop, 2 sails set, 3 reverse, 4 at the helm while idle.
     * See [Sailing] for the per-mode speed rules.
     */
    var moveMode: Int = 0
        internal set

    /** Whether the sails are set (mode 2). */
    val sailsSet: Boolean
        get() = moveMode == Sailing.MODE_SAILS

    /** Ticks left in the current wind gust (trim window), 0 when there is no gust. */
    internal var gustTicks: Int = 0

    /** Ticks until the next gust while sailing, -1 when not scheduled. */
    internal var nextGustIn: Int = -1

    /** Ticks elapsed in the current trim boost (drives the 4-tick trim loop anim). */
    internal var boostElapsed: Int = 0

    /** Current speed in fine units per tick. */
    var speed: Int = 0
        internal set

    /** Remaining ticks of the trim speed boost. */
    var boostTicks: Int = 0
        internal set

    /** South-west zone of the deck instance (the allocated dynamic area). */
    val instanceZoneX: Int
        get() = area.chunkX

    /** South-west zone of the deck instance (the allocated dynamic area). */
    val instanceZoneZ: Int
        get() = area.chunkY

    /** Root-world tile under the pivot, on the projected level. The scene (build area) follows this while aboard. */
    val rootTile: Location
        get() = Location(fineX shr 7, fineZ shr 7, level)

    /** Deck tile the player is placed on when boarding. */
    val boardTile: Location
        get() = deckTile(type.boardDx, type.boardDz, type.activeLevel)

    /** Absolute instance tile for a template-relative offset. */
    fun deckTile(dx: Int, dz: Int, level: Int): Location =
        Location((instanceZoneX shl 3) + dx, (instanceZoneZ shl 3) + dz, level)

    /** Whether [tile] lies inside this world entity's deck area (any level). */
    fun containsDeckTile(tile: Location): Boolean {
        val zoneX = tile.x shr 3
        val zoneZ = tile.y shr 3
        return zoneX >= instanceZoneX && zoneX < instanceZoneX + type.sizeX &&
                zoneZ >= instanceZoneZ && zoneZ < instanceZoneZ + type.sizeZ
    }

    fun moveTo(level: Int, fineX: Int, fineZ: Int, teleport: Boolean) {
        this.level = level
        this.fineX = fineX
        this.fineZ = fineZ
        avatar?.updateCoord(level, fineX, fineZ, teleport)
    }

    fun turnTo(angle: Int) {
        this.angle = angle and 2047
        avatar?.updateAngle(this.angle)
    }

    companion object {
        /** Tile coordinate to the fine coordinate of that tile's centre. */
        @JvmStatic
        fun tileToFine(tile: Int): Int = (tile shl 7) + 64
    }
}