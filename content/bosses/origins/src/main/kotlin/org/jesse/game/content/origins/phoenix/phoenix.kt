package org.jesse.game.content.origins.phoenix

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

class PhoenixNpcs : NPCDefinitionsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.8.2025
         */

        PHOENIX {
            hitpoints = 450
            aggressionType = PASSIVE
            attackSpeed = 5
            stats {
                combat(300, 300, 200, 300, 300)
            }
            attack {
                slash(31, 25039)
            }
            block {
                anim(25040)
            }
            spawn {
                deathAnim(25041)
            }
        }
    }
}
