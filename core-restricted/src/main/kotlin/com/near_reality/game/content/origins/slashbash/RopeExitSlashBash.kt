package com.near_reality.game.content.origins.slashbash

import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.plugins.dialogue.PlainChat

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

    override fun getObjects() = arrayOf(ObjectId.ROPE_12255)
}