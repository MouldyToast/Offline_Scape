package org.jesse.scripts.`object`.actions

import org.jesse.scripts.`object`.ObjectScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration

/**
 * @author Jire
 */
object ObjectActionCompilation : ScriptCompilationConfiguration(
    ObjectScriptCompilation
) {
    private fun readResolve(): Any = ObjectActionCompilation
}
