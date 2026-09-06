package com.near_reality.game.content.araxxor.objects

import com.zenyte.game.content.skills.slayer.slayer
import com.near_reality.game.content.araxxor.AraxxorInstance
import com.zenyte.game.GameConstants.WORLD_PROFILE
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId.WEB_TUNNEL_ARAXXOR
import com.zenyte.game.world.`object`.WorldObject

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
            val assignment = player.slayer().assignment
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