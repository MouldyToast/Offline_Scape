package org.jesse.scripts.player.actions

import org.jesse.game.world.entity.player.PlayerActionPlugin
import org.jesse.scripts.player.PlayerScript
import org.jesse.game.world.entity.player.Player
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Stan van der Bend
 */
@KotlinScript(
    "Player Action Script",
    fileExtension = "playeraction.kts",
    compilationConfiguration = PlayerActionCompilation::class
)
abstract class PlayerActionScript : PlayerActionPlugin(), PlayerScript {

    private var handleBody: (() -> Unit)? = null

    fun handle(body: () -> Unit) {
        handleBody = body
    }

    override fun handle() =
        handleBody?.invoke()?:Unit


    operator fun String.invoke(handle: Context.() -> Boolean) {
        bind(this) { player, npc ->
            Context(player, npc).handle()
        }
    }
}
