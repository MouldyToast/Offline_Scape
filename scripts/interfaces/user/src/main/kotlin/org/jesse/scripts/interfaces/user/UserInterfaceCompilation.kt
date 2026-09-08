package org.jesse.scripts.interfaces.user

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object UserInterfaceCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "org.jesse.game.model.ui.InterfacePosition.*",
            "org.jesse.game.GameInterface.*"
        )
    }) {
    private fun readResolve(): Any = UserInterfaceCompilation
}
