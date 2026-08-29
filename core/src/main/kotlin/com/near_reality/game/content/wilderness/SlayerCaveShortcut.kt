package com.near_reality.game.content.wilderness

import com.near_reality.game.content.offset
import com.zenyte.game.content.skills.agility.Shortcut
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.ForceMovement
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-07
 */
class SlayerCaveShortcut: Shortcut {

    override fun startSuccess(player: Player?, crevice: WorldObject?) {
        player ?: return; crevice ?: return
        player.faceObject(crevice)
        val movingSouth = player.location.y > crevice.location.y
        val offset: Int = if (crevice.location.y >= 10149) 5 else if (crevice.location.x > 3434) 4 else 3
        val endTile = crevice.location offset (if (movingSouth) Pair(0, -offset) else Pair(0, offset))
        schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.animation =  Animation(1237)
                    val movingDirection = if (movingSouth) ForceMovement.SOUTH else ForceMovement.NORTH
                    player.forceMovement = ForceMovement(endTile, 120, movingDirection)
                }
                else if (ticks == 3) {
                    player.setLocation(endTile)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int = 5
    override fun getSuccessXp(`object`: WorldObject?): Double = 10.0
    override fun getLevel(`object`: WorldObject?): Int  = 77
    override fun getObjectIds(): IntArray = intArrayOf(53259)
}