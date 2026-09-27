package org.jesse.game.world.entity.worldentity

import net.rsprot.protocol.game.outgoing.info.worldentityinfo.WorldEntityAvatar
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.region.dynamicregion.AllocatedArea

/**
 * A world entity: an instanced piece of map (the deck) rendered by the client at a
 * fine coordinate + angle in the root world.
 *
 * Coordinates are RSProt "fine" coordinates: 128 per tile, pivot at the tile centre
 * (`tile * 128 + 64`), matching the live capture's `.5` world entity coords.
 *
 * A player is "on" a world entity when their real coordinate lies inside its deck area
 * (the same rule RSProt uses) - there is no separate boarded flag.
 *
 * Generic: what the entity *is* (a boat, ...) and any behaviour belong to content,
 * which attaches its own state and hooks in through [WorldEntityListener].
 */
class WorldEntity internal constructor(
    val index: Int,
    val template: WorldEntityTemplate,
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
        get() = deckTile(template.boardDx, template.boardDz, template.activeLevel)

    /** Absolute instance tile for a template-relative offset. */
    fun deckTile(dx: Int, dz: Int, level: Int): Location =
        Location((instanceZoneX shl 3) + dx, (instanceZoneZ shl 3) + dz, level)

    /** Whether [tile] lies inside this world entity's deck area (any level). */
    fun containsDeckTile(tile: Location): Boolean {
        val zoneX = tile.x shr 3
        val zoneZ = tile.y shr 3
        return zoneX >= instanceZoneX && zoneX < instanceZoneX + template.sizeX &&
                zoneZ >= instanceZoneZ && zoneZ < instanceZoneZ + template.sizeZ
    }

    /**
     * Moves the pivot. Passengers stand still on the deck, so their own movement never checks for a
     * map reload: the scene (root tile) moves under them - trigger the same reload a walking player would get.
     */
    fun moveTo(level: Int, fineX: Int, fineZ: Int, teleport: Boolean) {
        this.level = level
        this.fineX = fineX
        this.fineZ = fineZ
        avatar?.updateCoord(level, fineX, fineZ, teleport)
        for (player in World.getPlayers()) {
            if (player != null && containsDeckTile(player.location) && player.needMapUpdate()) {
                player.setNeedRegionUpdate(true)
                player.setLoadingRegion(true)
            }
        }
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