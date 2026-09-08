package org.jesse.scripts.ground_items

import org.jesse.scripts.Script
import org.jesse.game.item.Item
import org.jesse.game.world.entity.Location
import org.jesse.game.world.flooritem.GlobalItem
import org.jesse.plugins.InitPlugin
import org.jesse.plugins.PluginPriority
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "Ground Item Spawn Script",
    "grounditems.kts",
    compilationConfiguration = GroundItemCompilation::class
)
@PluginPriority(500)
abstract class GroundItemSpawnScript : Script, InitPlugin {

    operator fun Int.invoke(
        amount: Int = 1,
        x: Int, y: Int, z: Int = 0,
        respawnTime: Int = 30
    ) {
        val item = Item(this, amount)
        val location = Location(x, y, z)
        val globalItem = GlobalItem(item, location, respawnTime)
        GlobalItem.createPersistentGlobalItemSpawn(globalItem)
    }

    operator fun Int.invoke(
        x: Int, y: Int, z: Int = 0,
        respawnTime: Int = 30
    ) = invoke(1, x, y, z, respawnTime)

}
