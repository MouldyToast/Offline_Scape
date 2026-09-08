package com.near_reality.game.content.origins.agedbarrelchest

import com.near_reality.scripts.npc.definitions.NPCDefinitionsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.Entity.EntityType
import com.zenyte.game.world.entity.Entity.EntityType.*
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType.*
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType.*
import com.zenyte.game.world.entity.npc.combatdefs.MonsterType
import com.zenyte.game.world.entity.npc.combatdefs.MonsterType.*
import com.zenyte.game.world.entity.npc.combatdefs.WeaknessType
import com.zenyte.game.world.entity.npc.combatdefs.WeaknessType.*
import com.zenyte.game.world.entity.Toxins.ToxinType
import com.zenyte.game.world.entity.Toxins.ToxinType.*
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.*
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.Projectile

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
