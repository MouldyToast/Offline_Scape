package org.jesse.game.content.skills.agility.pyramid.area

import com.google.common.eventbus.Subscribe
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.World
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.plugins.events.ServerLaunchEvent
import java.util.*

enum class MovingBlock(val npcId: Int, val objectId: Int, val spawn: Location, val direction: Direction) {
    FIRST_LEVEL_BLOCK(5788, 10872, ImmutableLocation(3372, 2847, 1), Direction.EAST), THIRD_LEVEL_BLOCK(
        5788,
        10873,
        ImmutableLocation(3366, 2845, 3),
        Direction.NORTH
    );

    companion object {
        val values: Array<MovingBlock?> = entries.toTypedArray()
        private val map = EnumMap<MovingBlock?, MovingBlockNPC>(MovingBlock::class.java)

        @JvmStatic
        @Subscribe
        fun onServerLaunch(event: ServerLaunchEvent?) {
            for (block in entries) {
                val npc = MovingBlockNPC(block.npcId, block.spawn, block.direction, 0)
                map.put(block, npc)
            }

            WorldTasksManager.scheduleCreation(runnable = Runnable {
                for (npc in map.values) {
                    npc.spawn()
                }
            })
        }

        fun moveBlocks() {
            for (entry in map.entries) {
                val key: MovingBlock = entry.key!!
                val value = entry.value
                value.slide(key.direction)
                World.sendSoundEffect(key.spawn, SoundEffect(1395, 5))
                schedule(WorldTask { value.slide(key.direction.getCounterClockwiseDirection(4)) }, 6)
            }
        }
    }
}
