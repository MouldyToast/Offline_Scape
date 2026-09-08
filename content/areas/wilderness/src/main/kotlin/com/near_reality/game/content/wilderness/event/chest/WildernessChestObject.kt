package com.near_reality.game.content.wilderness.event.chest

import com.zenyte.game.world.entity.Location
import com.zenyte.game.obj.ids.*
import com.zenyte.game.world.`object`.WorldObject

class WildernessChestObject(location: Location) : WorldObject(
    id = OBJECT_ID,
    tile = location
) {

    companion object {
        const val OBJECT_ID = STONE_CHEST_38519
    }
}
