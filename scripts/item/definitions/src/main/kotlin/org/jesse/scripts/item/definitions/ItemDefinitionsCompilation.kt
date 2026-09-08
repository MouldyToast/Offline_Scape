package org.jesse.scripts.item.definitions

import org.jesse.scripts.item.ItemScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object ItemDefinitionsCompilation : ScriptCompilationConfiguration(
    ItemScriptCompilation, body = {
        defaultImports(
            "org.jesse.game.world.entity.player.SkillConstants.*",
            "org.jesse.game.world.entity.player.container.impl.equipment.EquipmentType.*",
            "org.jesse.game.world.entity.player.Bonuses.Bonus.*",
        )
    }
) {
    private fun readResolve(): Any = ItemDefinitionsCompilation
}
