package com.near_reality.game.content.custom

import com.near_reality.game.item.CustomItemId
import com.near_reality.game.world.entity.player.action.combat.ISpecialAttack
import com.zenyte.game.GameConstants.isOwner
import com.zenyte.game.content.boons.impl.HammerDown
import com.zenyte.game.content.chambersofxeric.npc.Tekton
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript
import com.zenyte.game.world.entity.player.action.combat.SpecialType
import com.zenyte.game.world.entity.player.variables.TickVariable

/**
 * @author John J. Woloszyk / Kryeus
 * @date 6.30.2025
 */
object ChaoticSpecials {

    object Staff : ISpecialAttack {
        override val specialAttackName: String = "Crimson Lightning"
        override val weapons: IntArray = intArrayOf(CustomItemId.CHAOTIC_STAFF)
        override val delay: Int = 4
        override val type: SpecialType = SpecialType.MAGIC
        override val animation: Animation? = null
        override val graphics: Graphics? = null
        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            if(target is Player) {
                player.sendMessage("You cannot use this against other players.")
                return@SpecialAttackScript
            }
            if(target is NPC) {
                player.sendMessage("You activate Crimson Lighting affecting all of your combat targets.")
                combat.lastProjectileIndex = 1
                target.applyBlight()
                World.sendProjectile(
                    player.middleLocation,
                    target.middleLocation,
                    Projectile(2181, 32, 22, 0, 1, 90, 0, 0)
                )
            }
            player.variables.schedule(100, TickVariable.CRIMSON_LIGHTNING_TIMER)

        }
        override val attackType: AttackType = AttackType.MAGIC
    }

    object Longsword : ISpecialAttack {
        override val specialAttackName: String = "Fell Swoop"
        override val weapons: IntArray = intArrayOf(CustomItemId.CHAOTIC_LONGSWORD)
        override val delay: Int = 5
        override val type: SpecialType = SpecialType.MELEE
        override val animation: Animation? = null
        override val graphics: Graphics? = null
        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            if(target is Player) {
                player.sendMessage("You cannot use this against other players.")
                return@SpecialAttackScript
            }
            combat.delayHit(0, combat.getHit(player, target, 1.25, 1.0, 1.65, false))
        }
        override val attackType: AttackType = AttackType.MELEE
    }

    object Rapier : ISpecialAttack {
        override val specialAttackName: String = "Phantasmic Swipes"
        override val weapons: IntArray = intArrayOf(CustomItemId.CHAOTIC_RAPIER)
        override val delay: Int = 6
        override val type: SpecialType = SpecialType.MELEE
        override val animation: Animation = Animation(7514)
        override val graphics: Graphics? = null
        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            if(target is Player) {
                player.sendMessage("You cannot use this against other players.")
                return@SpecialAttackScript
            }
            val hit1 = Utils.random(50, 175).toDouble() / 100
            val hit2 = Utils.random(50, 150).toDouble() / 100
            val hit3 = Utils.random(50, 125).toDouble() / 100

            val acc1 = if(Utils.random(1, 10) == 1) 10000.0 else 1.00
            val acc2 = if(Utils.random(1, 10) == 1) 10000.0 else 1.00
            val acc3 = if(Utils.random(1, 10) == 1) 10000.0 else 1.00

            combat.delayHit(0, combat.getHit(player, target, acc1, 1.0, hit1, false))
            combat.delayHit(1, combat.getHit(player, target, acc2, 1.0, hit2, false))
            combat.delayHit(2, combat.getHit(player, target, acc3, 1.0, hit3, false))
        }
        override val attackType: AttackType = AttackType.MELEE
    }

    object Crossbow : ISpecialAttack {
        val projectile = Projectile(27, 38, 36, 41, 7, 5, 11, 5)
        override val specialAttackName: String = "Daemonheim Explosive"
        override val weapons: IntArray = intArrayOf(CustomItemId.CHAOTIC_CROSSBOW)
        override val delay: Int = 6
        override val type: SpecialType = SpecialType.RANGED
        override val animation: Animation? = null
        override val graphics: Graphics? = null
        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            if(target is Player) {
                player.sendMessage("You cannot use this against other players.")
                return@SpecialAttackScript
            }

            fun fireProjectile(projectile: Projectile?): Int {
                if (projectile == null) {
                    return 0
                }
                val startTile = Location(player.location)
                World.sendProjectile(startTile, target, projectile)
                return projectile.getTime(player.location.getAxisDistance(player.getSize(), target.location, target.getSize()))
            }

            val delay = fireProjectile(projectile)
            combat.delayHit(delay + 1, combat.getHit(player, target, 3.0, 3.0, 1.0, false))
            WorldTasksManager.schedule({
                target.graphics = Graphics(2138)
            }, delay)
            if(!isOwner(player))
                player.variables.schedule(100, TickVariable.DAEMONHEIM_EXPLOSIVE_COOLDOWN)
        }
        override val attackType: AttackType = AttackType.RANGED

        override fun testPredicate(player: Player): Boolean = player.variables.getTime(TickVariable.DAEMONHEIM_EXPLOSIVE_COOLDOWN) <= 0

    }

    object Maul : ISpecialAttack {
        val projectile = Projectile(27, 38, 36, 41, 7, 5, 11, 5)
        override val specialAttackName: String = "Meat Grinder"
        override val weapons: IntArray = intArrayOf(CustomItemId.CHAOTIC_MAUL)
        override val delay: Int = 6
        override val type: SpecialType = SpecialType.MELEE
        override val animation: Animation = Animation(11124)
        override val graphics: Graphics = Graphics(2804, 0, 0)
        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            if(target is Player) {
                player.sendMessage("You cannot use this against other players.")
                return@SpecialAttackScript
            }

            player.sendSound(SpecialAttackScript.SHIELD_BASH_SOUND)
            val hit = combat.getHit(player, target, 1.25, 1.5, 1.0, false)
            val shouldApplyHit = combat.isSuccessful(player, target, 1.0, AttackType.CRUSH)


            if (shouldApplyHit) {
                combat.delayHit(0, hit)
                if (hit.damage > 0) {
                    target.drainSkill(SkillConstants.DEFENCE, 45.0)
                }
            }
        }
        override val attackType: AttackType = AttackType.MELEE
    }


}