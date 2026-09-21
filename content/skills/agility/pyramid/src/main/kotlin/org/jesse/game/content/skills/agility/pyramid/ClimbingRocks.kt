package org.jesse.game.content.skills.agility.pyramid

import org.jesse.game.content.achievementdiary.diaries.DesertDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.item.Item
import org.jesse.game.item.ids.PYRAMID_TOP
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import org.jesse.plugins.dialogue.ItemChat

class ClimbingRocks : AgilityCourseObstacle(AgilityPyramid::class.java, 6) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(climbingAnim)
        player.sendSound(SoundEffect(2454))
        schedule(WorldTask {
            if (player.getVarManager().getBitValue(AgilityPyramid.Companion.HIDE_PYRAMID_VARBIT) == 0) {
                val inventory = player.getInventory()
                if (inventory.hasFreeSlots()) {
                    schedule(WorldTask {
                        val reward: Item = Item(PYRAMID_TOP)
                        player.getDialogueManager().start(ItemChat(player, reward, "You find a golden pyramid!"))
                        player.getVarManager().sendBit(AgilityPyramid.Companion.HIDE_PYRAMID_VARBIT, true)
                        inventory.addOrDrop(reward)
                        player.getAchievementDiaries().update(DesertDiary.CLIMB_AGILITY_PYRAMID)
                    })
                } else {
                    player.sendMessage("You don\'t have enough inventory space to pick up this item.")
                }
            } else {
                player.sendMessage("You find nothing on top of the pyramid.")
            }
        })
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 0.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getObjectIds(): IntArray = intArrayOf(10851)

    companion object {
        private val climbingAnim = Animation(3063)
    }
}
