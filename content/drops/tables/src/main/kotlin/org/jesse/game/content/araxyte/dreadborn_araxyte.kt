package org.jesse.game.content.araxyte

import org.jesse.game.npc.ids.*
import java.util.*
import org.jesse.scripts.npc.definitions.NPCDefinitionsScript
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
