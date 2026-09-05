package com.near_reality.game.content.origins.planefreezer

import com.near_reality.scripts.npc.definitions.NPCDefinitionsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.Entity.EntityType
import com.zenyte.game.world.entity.Entity.EntityType.*
import com.near_reality.game.item.CustomNpcId
import com.near_reality.game.item.CustomNpcId.*
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
