package com.near_reality.game.world.entity.player.action.combat.ranged

import com.near_reality.game.content.custom.GodBow
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.player.action.combat.RangedCombat
import com.zenyte.game.world.entity.player.action.combat.ranged.ammunition.ChargedAmmunitionSource

/**
 * Represents a [RangedCombat] implementation for a [GodBow].
 *
 * The only thing this does it override ammo handling behaviour.
 *
 * @author Stan van der Bend
 */
class GodBowCombat(target: Entity, private val bow: GodBow) : RangedCombat(target) {

    override fun dropAmmunition(delay: Int, destroy: Boolean) = Unit

    override fun applyAmmo(): Boolean {
        ammunitionSource = ChargedAmmunitionSource(player, bow)
        return true
    }

}
