package org.jesse.game.content.araxxor.objects

import org.jesse.game.content.araxxor.AraxxorInstance
import org.jesse.game.GameConstants.WORLD_PROFILE
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-23
 */
class AraxxorTunnelEntrance : ObjectAction {
    override fun handleObjectAction(player: Player?, `object`: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; `object` ?: return

        if(player.isDeveloper || WORLD_PROFILE.isBeta()) {
            player.sendMessage("Zaros himself permits you to enter this cave right now.")
            AraxxorInstance(player).constructRegion()
        } else {
            val assignment = player.slayer.assignment
            if (assignment != null) {
                val task = assignment.task
                if (task != null) {
                    val monsters = listOf(task.monsters)[0]
                    if (monsters.contains("araxxor"))
                        AraxxorInstance(player).constructRegion()
                    else denyNoTask(player)
                }
                else denyNoTask(player)
            }
            else denyNoTask(player)
        }

    }

    private fun denyNoTask(player: Player) {
        player.dialogue { plain("You need to be on a task to fight this boss.") }
    }

    override fun getObjects(): Array<Any> = arrayOf(WEB_TUNNEL_ARAXXOR)
}