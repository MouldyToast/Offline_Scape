package org.jesse.game.content.boss.nex.npc

import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.Spawnable

/**
 * Represents a [Spawnable] [BloodReaver] npc instance.
 *
 * This NPC can be found in the Ancient Prison, outside of Nex's dungeon.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class SpawnableBloodReaver(id: Int, tile: Location?, facing: Direction, radius: Int) :
    BloodReaver(id, tile, facing, radius), Spawnable {

    override fun validate(id: Int, name: String) = id == BLOOD_REAVER
}
