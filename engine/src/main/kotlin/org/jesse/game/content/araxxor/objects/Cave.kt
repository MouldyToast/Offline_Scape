package org.jesse.game.content.araxxor.objects

import org.jesse.game.content.North
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-23
 */
class Cave : ObjectAction {
    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        player ?: return
        `object` ?: return
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(Location(3679, 9797))
            player.faceDirection(Direction.NORTH)
        }
        screen.fade()
        player.animation = Animation.CRAWL
        schedule(1) {
            player.forceMovement = ForceMovement(Location(3658, 3409), 30, North.direction)
        }
        schedule(2) {
            screen.unfade()
            player.unlock()
        }

    }

    override fun getObjects(): Array<Any> = arrayOf(CAVE_42594)
}