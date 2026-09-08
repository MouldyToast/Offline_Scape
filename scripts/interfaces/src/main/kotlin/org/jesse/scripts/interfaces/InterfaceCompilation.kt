package org.jesse.scripts.interfaces

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.with

/**
 * @author Jire
 */
object InterfaceCompilation : ScriptCompilationConfiguration(
    DefaultCompilation.with {
        defaultImports(
            "org.jesse.game.model.ui.InterfacePosition.*",

            "org.jesse.game.GameInterface",
            "org.jesse.game.GameInterface.*",

            "org.jesse.game.util.AccessMask",
            "org.jesse.game.util.AccessMask.*",

            "mgi.types.config.enums.Enums",
            "mgi.types.config.enums.Enums.*",
        )
    }
) {
    private fun readResolve(): Any = InterfaceCompilation
}