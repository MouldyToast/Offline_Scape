package com.near_reality.game.content.araxyte

import com.zenyte.game.npc.ids.*
import java.util.*
import com.near_reality.scripts.npc.definitions.NPCDefinitionsScript
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

class DreadbornAraxyteNpcs : NPCDefinitionsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 8.7.2025
         */

        DREADBORN_ARAXYTE {
            hitpoints = 350
            stats {
                combat(260, 260, 100, 100, 1)
                aggressive(200, 200, 200, 0, 0)
                defensive(60, 30, 0, 10, 100)
                strength(10, 0, 0)
            }
            attack {
                anim(11497)
                maxHit = 31
                immunityTypes = (EnumSet.of(ImmunityType.VENOM))
            }
            spawn {
                spawnAnimation = Animation(11482)
            }
        }
    }
}
