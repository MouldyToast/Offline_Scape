package org.jesse.game.content.origins.slashbash

import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.plugins.dialogue.PlainChat

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.11.2025
 */
class RopeExitSlashBash : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        when(option) {
            "Climb-up" -> {
                player.lock()
                val screen = FadeScreen(player) {
                    player.setLocation(Location(3169, 3171, 0))
                    player.faceDirection(Direction.NORTH)
                    player.dialogueManager.start(PlainChat(player, "and find your way out into the Lumbridge swamp."))
                }
                player.dialogueManager.start(PlainChat(player, "You climb up the rope and through some tunnels...", false))
                screen.fade()
                player.direction = Direction.SOUTH.direction
                player.animation = Animation.LADDER_UP
                schedule(2) {
                    screen.unfade()
                    player.unlock()
                }
            }
        }
    }

    override fun getObjects() = arrayOf(ROPE_12255)
}