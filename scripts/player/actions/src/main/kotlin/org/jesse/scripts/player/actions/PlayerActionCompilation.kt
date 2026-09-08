package org.jesse.scripts.player.actions

import org.jesse.scripts.player.PlayerScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration

/**
 * @author Stan van der bend
 */
object PlayerActionCompilation : ScriptCompilationConfiguration(PlayerScriptCompilation) {
    private fun readResolve(): Any = PlayerActionCompilation
}
