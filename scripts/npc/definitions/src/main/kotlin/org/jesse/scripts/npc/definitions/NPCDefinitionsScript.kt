package org.jesse.scripts.npc.definitions

import org.jesse.game.content.araxxor.attacks.Attack
import org.jesse.scripts.npc.NPCScript
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.npc.combatdefs.*
import org.jesse.plugins.InitPlugin
import org.jesse.plugins.PluginPriority
import javassist.bytecode.analysis.ControlFlow.Block
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "NPC Definitions Script",
    "npcs.kts",
    compilationConfiguration = NPCDefinitionsCompilation::class
)
@PluginPriority(1_000)
abstract class NPCDefinitionsScript : NPCScript, InitPlugin {

    operator fun Int.invoke(build: NPCCombatDefinitions.() -> Unit) {
        val def = NPCCombatDefinitions().apply {
            id = this@invoke

            build()
        }
        NPCCDLoader.insert(this, def)
    }

    fun NPCCombatDefinitions.stats(build: StatDefinitions.() -> Unit) {
        statDefinitions = (statDefinitions?:StatDefinitions()).apply(build)
    }

    fun NPCCombatDefinitions.attack(build: AttackDefinitions.() -> Unit) {
        attackDefinitions = (attackDefinitions?:AttackDefinitions()).apply(build)
    }

    fun NPCCombatDefinitions.block(build: BlockDefinitions.() -> Unit) {
        blockDefinitions = (blockDefinitions?:BlockDefinitions()).apply(build)
    }

    fun NPCCombatDefinitions.spawn(build: SpawnDefinitions.() -> Unit) {
        spawnDefinitions = (spawnDefinitions?:SpawnDefinitions()).apply(build)
    }

    fun StatDefinitions.combat(
        attack: Int = 1,
        strength: Int = 1,
        defense: Int = 1,
        magic: Int = 1,
        range: Int = 1,
        all: Int = 0
    ) = run {
        if(all != 0) {
            combatStats[0] = all
            combatStats[1] = all
            combatStats[2] = all
            combatStats[3] = all
            combatStats[4] = all
        } else {
            combatStats[0] = attack
            combatStats[1] = strength
            combatStats[2] = defense
            combatStats[3] = magic
            combatStats[4] = range
        }
    }


    fun StatDefinitions.aggressive(
        stab: Int = 0,
        slash: Int = 0,
        crush: Int = 0,
        magic: Int = 0,
        range: Int = 0,
        all: Int = 0
    ) = run {
        if (all != 0) {
            aggressiveStats[0] = all
            aggressiveStats[1] = all
            aggressiveStats[2] = all
            aggressiveStats[3] = all
            aggressiveStats[4] = all
        } else {
            aggressiveStats[0] = stab
            aggressiveStats[1] = slash
            aggressiveStats[2] = crush
            aggressiveStats[3] = magic
            aggressiveStats[4] = range
        }
    }

    fun StatDefinitions.defensive(
        stab: Int = 0,
        slash: Int = 0,
        crush: Int = 0,
        magic: Int = 0,
        range: Int = 0,
        all: Int = 0
    ) = run {
        if (all != 0) {
            defensiveStats[0] = all
            defensiveStats[1] = all
            defensiveStats[2] = all
            defensiveStats[3] = all
            defensiveStats[4] = all
        } else {
            defensiveStats[0] = stab
            defensiveStats[1] = slash
            defensiveStats[2] = crush
            defensiveStats[3] = magic
            defensiveStats[4] = range
        }
    }

    fun StatDefinitions.strength(
        melee: Int = 0,
        range: Int = 0,
        magic: Int = 0
    ) = run {
        otherBonuses[0] = melee
        otherBonuses[1] = range
        otherBonuses[2] = magic
    }

    fun AttackDefinitions.anim(id: Int) = run { this.animation = Animation(id) }

    fun BlockDefinitions.anim(id: Int) = run { this.animation = Animation(id) }
    fun SpawnDefinitions.deathAnim(id: Int) = run { this.deathAnimation = Animation(id) }

    fun AttackDefinitions.magic(max: Int, anim: Int) = run {
        this.type = AttackType.MAGIC
        this.maxHit = max
        anim(anim)
    }

    fun AttackDefinitions.stab(max: Int, anim: Int) = run {
        this.type = AttackType.STAB
        this.maxHit = max
        anim(anim)
    }

    fun AttackDefinitions.crush(max: Int, anim: Int) = run {
        this.type = AttackType.CRUSH
        this.maxHit = max
        anim(anim)
    }

    fun AttackDefinitions.slash(max: Int, anim: Int) = run {
        this.type = AttackType.SLASH
        this.maxHit = max
        anim(anim)
    }

    fun AttackDefinitions.range(max: Int, anim: Int) = run {
        this.type = AttackType.RANGED
        this.maxHit = max
        anim(anim)
    }

    fun AttackDefinitions.melee(max: Int, anim: Int) = run {
        this.type = AttackType.MELEE
        this.maxHit = max
        anim(anim)
    }
}
