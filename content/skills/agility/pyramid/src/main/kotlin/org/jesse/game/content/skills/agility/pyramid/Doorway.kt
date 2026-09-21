package org.jesse.game.content.skills.agility.pyramid

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import org.jesse.plugins.dialogue.PlainChat

class Doorway : AgilityCourseObstacle(AgilityPyramid::class.java, 7) {
    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 0
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setLocation(Location(3364, 2830, 0))
        player.getDialogueManager()
            .start(PlainChat(player, "You climb down the steep passage. It leads to the base of the<br>pyramid"))
        player.getVarManager().sendBit(AgilityPyramid.Companion.HIDE_PYRAMID_VARBIT, false)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 0.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getObjectIds(): IntArray = intArrayOf(10855, 10856)
}
