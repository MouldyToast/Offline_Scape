package com.near_reality.game.content.origins.bork

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
