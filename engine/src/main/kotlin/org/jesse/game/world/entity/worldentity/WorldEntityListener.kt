package org.jesse.game.world.entity.worldentity

import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player

/**
 * Content hooks into the world entity lifecycle. Register with [WorldEntities.addListener]
 * (typically from a `ServerLaunchEvent` subscriber). Every hook runs on the world thread and
 * is called for every live world entity - content filters out the ones it does not own.
 */
interface WorldEntityListener {

    /** Once per game tick for each live world entity, before the info protocols are built. */
    fun onTick(entity: WorldEntity) {}

    /** SET_HEADING from [player], who is standing on [entity]'s deck. [heading] is the raw 0..15 value. */
    fun onSetHeading(player: Player, entity: WorldEntity, heading: Int) {}

    /**
     * [entity] is despawning. Called after its passengers were evacuated ([onEvacuate]) and while the entity is
     * still registered; the deck and avatar are released right after. Drop any state attached to the entity here.
     */
    fun onDespawn(entity: WorldEntity) {}

    /**
     * [player] is being taken off [entity]'s deck because the entity despawns ([logout] false) or the player
     * logs out ([logout] true). Called while the player is still on the deck; the engine moves them afterwards.
     * On a despawn this runs for every passenger before [onDespawn].
     */
    fun onEvacuate(player: Player, entity: WorldEntity, logout: Boolean) {}

    /**
     * Where an evacuated [player] should land. The first non-null answer across listeners wins;
     * if every listener returns null the player lands on [WorldEntity.rootTile].
     */
    fun evacuationTile(player: Player, entity: WorldEntity, logout: Boolean): Location? = null
}