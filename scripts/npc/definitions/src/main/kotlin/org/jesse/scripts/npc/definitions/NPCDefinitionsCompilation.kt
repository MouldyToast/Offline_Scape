package org.jesse.scripts.npc.definitions

import org.jesse.scripts.CoreCompilation
import org.jesse.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports


/**
 * @author Jire
 */
object NPCDefinitionsCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, CoreCompilation, body = {
        defaultImports(
            "org.jesse.game.world.entity.Entity.EntityType",
            "org.jesse.game.world.entity.Entity.EntityType.*",

            "org.jesse.game.npc.ids.*",

            "org.jesse.game.world.entity.npc.combatdefs.ImmunityType",
            "org.jesse.game.world.entity.npc.combatdefs.ImmunityType.*",

            "org.jesse.game.world.entity.npc.combatdefs.AggressionType",
            "org.jesse.game.world.entity.npc.combatdefs.AggressionType.*",

            "org.jesse.game.world.entity.npc.combatdefs.MonsterType",
            "org.jesse.game.world.entity.npc.combatdefs.MonsterType.*",

            "org.jesse.game.world.entity.npc.combatdefs.WeaknessType",
            "org.jesse.game.world.entity.npc.combatdefs.WeaknessType.*",

            "org.jesse.game.world.entity.Toxins.ToxinType",
            "org.jesse.game.world.entity.Toxins.ToxinType.*",

            "org.jesse.game.world.entity.npc.combatdefs.AttackType",
            "org.jesse.game.world.entity.npc.combatdefs.AttackType.*",
        )
    }
) {
    private fun readResolve(): Any = NPCDefinitionsCompilation
}