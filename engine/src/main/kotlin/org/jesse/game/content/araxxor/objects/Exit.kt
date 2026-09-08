package org.jesse.game.content.araxxor.objects

import org.jesse.game.content.South
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
class Exit : ObjectAction {
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
        player.animation = Animation.CRAWL
        val screen = FadeScreen(player) {
            player.setLocation(Location(3658, 3407))
            player.faceDirection(Direction.SOUTH)
        }
        screen.fade()
        player.animation = Animation.CRAWL
        schedule(1) {
            player.forceMovement = ForceMovement(Location(3679, 9795), 30, South.direction)
        }
        schedule(2) {
            screen.unfade()
            player.unlock()
        }

    }

    override fun getObjects(): Array<Any> = arrayOf(CAVE_42595)
}