package org.jesse.game.content.elven.obj

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * Handles the entrance and exit of the Trahaearn mine.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class TrahaearnMineCaveAction : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        when(option) {
            "Enter" -> {
                val screen = FadeScreen(player) { player.setLocation(Location(3302, 12454)) }
                screen.fade()
                player.animation = Animation(2796)
                player.sendSound(SoundEffect(2454, 0, 4))
                WorldTasksManager.schedule({ screen.unfade() }, 2)
            }
            "Exit" -> {
                val screen = FadeScreen(player) { player.setLocation(Location(3271, 6051)) }
                screen.fade()
                player.animation = Animation.LADDER_UP
                WorldTasksManager.schedule({ screen.unfade() }, 2)
            }
        }
    }

    override fun getObjects() = arrayOf(CAVE_ENTRANCE_36556, STEPS_36215)
}
