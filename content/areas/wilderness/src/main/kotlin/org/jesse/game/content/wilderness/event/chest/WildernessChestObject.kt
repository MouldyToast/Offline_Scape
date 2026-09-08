package org.jesse.game.content.wilderness.event.chest

import org.jesse.game.world.entity.Location
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

class WildernessChestObject(location: Location) : WorldObject(
    id = OBJECT_ID,
    tile = location
) {

    companion object {
        const val OBJECT_ID = STONE_CHEST_38519
    }
}
