package org.jesse.game.content.origins.planefreezer

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

class PlanefreezersNpcs : NPCDefinitionsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.7.2025
         */

        PLANE_FREEZER_LAKHRAHNAZ {
            hitpoints = 350
            attackSpeed = 6
            aggressionType = AGGRESSIVE
            stats {
                combat(attack = 1, strength = 1, defense = 140, magic = 300, range = 175)
                defensive(221, 235, 222, 0, 221)
            }
            block {
                anim(25023)
            }
            spawn {
                respawnDelay = 40
                deathAnim(25024)
            }
        }
    }
}
