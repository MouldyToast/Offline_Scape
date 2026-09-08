package org.jesse.game.content.elven.obj

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.1.2025
 */
class IorwerthMidShortcut: ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        when(`object`.id) {
            36692, 36693 -> if(player.skills.getLevel(SkillConstants.AGILITY) < 78) {
                player.sendMessage("You need at least level 78 agility to do this.")
                return
            }
            36694, 36695 -> if(player.skills.getLevel(SkillConstants.AGILITY) < 84) {
                player.sendMessage("You need at least level 84 agility to do this.")
                return
            }
        }

        player.animation = Animation.CRAWL
        when(`object`.id) {
            36692 -> WorldTasksManager.schedule { player.lock(1); player.setLocation(Location(3216, 12441, 0)) }
            36693 -> WorldTasksManager.schedule { player.lock(1); player.setLocation(Location(3222, 12441, 0)) }
            36694 -> WorldTasksManager.schedule { player.lock(1); player.setLocation(Location(3232, 12420, 0)) }
            36695 -> WorldTasksManager.schedule { player.lock(1); player.setLocation(Location(3242, 12420, 0)) }
        }

    }

    override fun getObjects(): Array<Any> = arrayOf(36692, 36693, 36694, 36695)
}