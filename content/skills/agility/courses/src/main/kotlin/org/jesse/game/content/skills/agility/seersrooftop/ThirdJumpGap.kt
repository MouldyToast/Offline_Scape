package org.jesse.game.content.skills.agility.seersrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.item.Item
import org.jesse.game.obj.ids.GAP_14930
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import org.jesse.plugins.dialogue.ItemChat

class ThirdJumpGap : AgilityCourseObstacle(SeersRooftopCourse::class.java, 5) {
    override fun preconditions(player: Player, `object`: WorldObject?): Boolean {
        if (player.getBooleanAttribute("SeersTrapdoor")) {
            player.getDialogueManager().start(ItemChat(player, IMAGE, "This course begins at the bank."))
            return false
        }
        return true
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setFaceLocation(Location(player.getX(), player.getY() - 1, 0))
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.setAnimation(Animation.LEAP)
                else if (ticks == 1) {
                    player.setAnimation(Animation.LAND)
                    player.setLocation(FINISH)
                    MarkOfGrace.spawn(player, SeersRooftopCourse.Companion.MARK_LOCATIONS, 60, 20)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 60
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14930)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 25.0
    }

    companion object {
        private val IMAGE = Item(6517)
        private val FINISH = Location(2701, 3465, 2)
    }
}
