package org.jesse.game.world.entity.player.action.combat

import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.SpecialAttackScript
import org.jesse.game.world.entity.player.action.combat.SpecialType

/**
 * Represents a special attack a wield-able item can perform,
 * usually takes x amount of charges.
 *
 * @see org.jesse.game.world.entity.player.action.combat.SpecialAttack main implementation.
 *
 * @author Stan van der Bend
 */
interface ISpecialAttack {
    fun testPredicate(player: Player): Boolean = true

    /**
     * The [name][String] of this special attack.
     */
    val specialAttackName: String

    /**
     * An array of item ids for which this special attack applies.
     */
    val weapons: IntArray

    /**
     * Overrides the weapon speed for the special attack.
     *
     * Or use [SpecialAttackScript.WEAPON_SPEED] to use the same base speed as the weapon used.
     */
    val delay: Int

    /**
     * What combat style is being used (range, mage, melee)
     */
    val type: SpecialType

    /**
     * An optional [Animation] to play at start of the attack.
     */
    val animation: Animation?

    /**
     * An optional [Graphics] to play at start of the attack.
     */
    val graphics: Graphics?

    /**
     * Handles the attack behaviour.
     */
    val attack: SpecialAttackScript

    /**
     * Defines the combat bonuses applied during hit calculation.
     */
    val attackType: AttackType
}
