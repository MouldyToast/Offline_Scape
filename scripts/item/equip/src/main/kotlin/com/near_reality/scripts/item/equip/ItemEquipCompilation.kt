package com.near_reality.scripts.item.equip

import com.near_reality.scripts.item.ItemScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration

/**
 * Represents the compilation configuration for [equip scripts][ItemEquipScript].
 *
 * @author Stan van der Bend
 */
object ItemEquipCompilation : ScriptCompilationConfiguration(
    ItemScriptCompilation, body = {
    }
) {
    private fun readResolve(): Any = ItemEquipCompilation
}
