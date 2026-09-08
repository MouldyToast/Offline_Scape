package com.near_reality.game.content.origins.phoenix

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
