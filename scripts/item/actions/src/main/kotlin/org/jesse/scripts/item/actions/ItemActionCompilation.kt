package org.jesse.scripts.item.actions

import org.jesse.scripts.item.ItemScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration

/**
 * @author Jire
 */
object ItemActionCompilation : ScriptCompilationConfiguration(
    ItemScriptCompilation, body = {
    }
) {
    private fun readResolve(): Any = ItemActionCompilation
}
