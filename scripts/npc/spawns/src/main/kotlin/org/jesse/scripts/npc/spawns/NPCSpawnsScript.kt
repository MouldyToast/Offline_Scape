package org.jesse.scripts.npc.spawns

import org.jesse.scripts.npc.NPCScript
import org.jesse.game.util.Direction
import org.jesse.game.util.Direction.SOUTH
import org.jesse.game.world.entity.npc.spawns.NPCSpawn
import org.jesse.game.world.entity.npc.spawns.NPCSpawnLoader
import org.jesse.plugins.InitPlugin
import org.jesse.plugins.PluginPriority
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "NPC Spawns Script",
    fileExtension = "spawns.kts",
    compilationConfiguration = NPCSpawnsCompilation::class
)
@PluginPriority(500)
abstract class NPCSpawnsScript : NPCScript, InitPlugin {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    var xOffsets = mutableListOf<Int>()
    var yOffsets = mutableListOf<Int>()

    operator fun Int.invoke(
        isX: Boolean = true
    ) {
        if(isX) {
            xOffsets.add(this)
        } else {
            yOffsets.add(this)
        }
    }

    operator fun Int.invoke(
        x: Int, y: Int, z: Int = 0,
        direction: Direction = SOUTH,
        walkRadius: Int = 0
    )  {
        if(xOffsets.isNotEmpty()) {
            for(xOff in xOffsets) NPCSpawnLoader.addSpawn(NPCSpawn(this, x + (64 * xOff), y, z, direction, walkRadius))
        }

        if(yOffsets.isNotEmpty()) {
            for(yOff in yOffsets) NPCSpawnLoader.addSpawn(NPCSpawn(this, x, y + (64 * yOff), z, direction, walkRadius))
        }

        NPCSpawnLoader.addSpawn(NPCSpawn(this, x, y, z, direction, walkRadius))
    }

}