package org.jesse.game.content.origins.bork

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

class BorkNpcs : NPCDefinitionsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.7.2025
         */
        BORK {
            hitpoints = 350
            aggressionType = AGGRESSIVE
            aggressionDistance = 10
            attackSpeed = 7

            stats {
                combat(all = 150)
                aggressive(all = 148)
                strength(melee = 60)
            }
            attack {
                crush(23, 25017)
            }
            block { anim(25018) }
            spawn {
                deathAnim(25019)
                respawnDelay = 40
            }
        }
    }
}
