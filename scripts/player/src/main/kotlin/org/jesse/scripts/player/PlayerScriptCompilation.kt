package org.jesse.scripts.player

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration

/**
 * Represents a [compilation configuration][ScriptCompilationConfiguration] for [player scripts][PlayerScript].
 *
 * @author Stan van der Bend
 */
object PlayerScriptCompilation : ScriptCompilationConfiguration(DefaultCompilation) {
    private fun readResolve(): Any = PlayerScriptCompilation
}
