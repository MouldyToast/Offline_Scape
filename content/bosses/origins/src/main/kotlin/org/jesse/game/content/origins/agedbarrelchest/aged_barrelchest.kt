package org.jesse.game.content.origins.agedbarrelchest

import org.jesse.scripts.npc.definitions.NPCDefinitionsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.Entity.EntityType
import org.jesse.game.world.entity.Entity.EntityType.*
import org.jesse.game.world.entity.npc.combatdefs.ImmunityType
import org.jesse.game.world.entity.npc.combatdefs.ImmunityType.*
import org.jesse.game.world.entity.npc.combatdefs.AggressionType
import org.jesse.game.world.entity.npc.combatdefs.AggressionType.*
import org.jesse.game.world.entity.npc.combatdefs.MonsterType
import org.jesse.game.world.entity.npc.combatdefs.MonsterType.*
import org.jesse.game.world.entity.npc.combatdefs.WeaknessType
import org.jesse.game.world.entity.npc.combatdefs.WeaknessType.*
import org.jesse.game.world.entity.Toxins.ToxinType
import org.jesse.game.world.entity.Toxins.ToxinType.*
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.npc.combatdefs.AttackType.*
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.Projectile

class AgedBarrelchestNpcs : NPCDefinitionsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.8.2025
         */

        AGED_BARRELCHEST {
            hitpoints = 500
            attackSpeed = 6
            aggressionType = AGGRESSIVE
            stats {
                combat(120, 120, 120, 1, 1)
                aggressive(68, 0, 82, 0, 0)
                defensive(221, 235, 222, 100, 221)
                strength(72)
            }
            attack { crush(31, 25049) }
            block { anim(25050) }
            spawn { deathAnim(25051) }
        }
    }
}
