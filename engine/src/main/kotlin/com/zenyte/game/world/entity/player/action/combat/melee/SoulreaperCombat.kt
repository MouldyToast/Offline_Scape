package com.zenyte.game.world.entity.player.action.combat.melee

import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.VarManager
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.entity.player.variables.TickVariable
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @author Khaled Abdeljaber
 */
class SoulreaperCombat(target: Entity) : MeleeCombat(target) {


    override fun extra(hit: Hit) {
        super.extra(hit)

        player.variables.schedule(20, TickVariable.SOULREAPER_TIMER, true)

        val charges = min(5.0, player.soulreaperCharges.toDouble()).toInt()
        hit.damage = (hit.damage * (1.0 + (charges * 0.050))).roundToInt()

        if (charges == 5) {
            return
        }

        player.soulreaperCharges = min((charges + 1), 5)
        player.applyHit(Hit(8, HitType.REGULAR))
    }

    companion object {
        val HUMAN_SOULREAPER_SPECIAL_ATTACK_SFX = SoundEffect(7019)

        const val SOULREAPER_CHARGES_VARP = 3784
        var Player.soulreaperCharges
            get() = varManager.getValue(SOULREAPER_CHARGES_VARP)
            set(value) {
                varManager.sendVar(SOULREAPER_CHARGES_VARP, value)
                if (value == 0) {
                    variables.schedule(0, TickVariable.SOULREAPER_TIMER, true)
                }
            }

        init {
            VarManager.appendPersistentVarp(SOULREAPER_CHARGES_VARP)
        }

        @JvmStatic
        fun special(player: Player, combat: PlayerCombat, target: Entity) {
            val charges = player.soulreaperCharges
            if (charges == 0) {
                return
            }

            val hit = combat.getHit(player, target, 1.0 + (charges * 0.69), 1.0, 1.0 + (charges * 0.06), false)
            combat.delayHit(target, 0, hit)

            player.sendSound(HUMAN_SOULREAPER_SPECIAL_ATTACK_SFX)
            player.soulreaperCharges = 0
            player.applyHit(Hit(charges * 8, HitType.HEALED))
        }

        @JvmStatic
        fun handleTimer(player: Player) {
            val charges: Int = player.soulreaperCharges - 1
            if (charges < 0) {
                return
            }

            if (player.hitpoints < player.maxHitpoints) {
                player.sendFilteredMessage("You gain some health back after losing a Soul stack.")
                player.applyHit(Hit(8, HitType.HEALED))
            }
            player.soulreaperCharges = charges
            if (charges == 0) {
                return
            }
            player.variables.schedule(20, TickVariable.SOULREAPER_TIMER, true)
        }
    }


}