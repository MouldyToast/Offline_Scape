package org.jesse.game.world.entity.worldentity

/**
 * A loc placed on a world entity's deck, relative to the template's south-west tile.
 * [opFlags] is the initial right-click op mask (bit n = op n+1) sent with the loc; change it at runtime with
 * [WorldEntity.setLocOpFlags].
 */
data class DeckLoc(
    val id: Int,
    val shape: Int,
    val rotation: Int,
    val dx: Int,
    val dz: Int,
    val level: Int,
    val opFlags: Int = DeckLoc.ALL_OPS,
) {
    companion object {
        /** All five ops shown (`OpFlags.ALL_SHOWN`). */
        const val ALL_OPS = 0b11111
    }
}

/**
 * The map template a world entity is built from. The engine copies [sizeX] x [sizeZ] zones starting at
 * template zone ([templateZoneX], [templateZoneZ]) into a dynamic area on all levels, spawns [deck] on it,
 * and allocates the RSProt avatar with [configId] / [activeLevel].
 *
 * Content owns the concrete templates (e.g. sailing's boat hulls); the engine knows nothing about them.
 *
 * @param configId `worldentity` config id (cache `[worldentity_N]`).
 * @param templateZoneX south-west template zone X.
 * @param templateZoneZ south-west template zone Z.
 * @param sizeX size in zones.
 * @param sizeZ size in zones.
 * @param activeLevel level the client treats as the deck's main level (`mainlevel`).
 * @param boardDx deck tile (template-relative) a player is placed on when boarding.
 * @param boardDz deck tile (template-relative) a player is placed on when boarding.
 * @param deck dynamic locs spawned on the deck after the template copy.
 */
class WorldEntityTemplate(
    val configId: Int,
    val templateZoneX: Int,
    val templateZoneZ: Int,
    val sizeX: Int,
    val sizeZ: Int,
    val activeLevel: Int,
    val boardDx: Int,
    val boardDz: Int,
    val deck: List<DeckLoc>,
) {
    override fun toString(): String =
        "WorldEntityTemplate(configId=$configId, template=($templateZoneX, $templateZoneZ), size=${sizeX}x$sizeZ)"
}