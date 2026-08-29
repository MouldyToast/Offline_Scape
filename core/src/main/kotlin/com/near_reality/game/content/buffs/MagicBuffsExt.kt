package com.near_reality.game.content.buffs

import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import com.zenyte.game.world.entity.player.action.combat.MagicCombat
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import kotlin.math.floor

fun Player.spell() : CombatSpell? = if(this.actionManager.action !is MagicCombat) null else ((this.actionManager.action as MagicCombat).spell)
fun Player.target() : Entity? = if(this.actionManager.action !is PlayerCombat) null else (this.actionManager.action as PlayerCombat).target

fun Player.magicDartDamage(target: Entity): Double {
    val staffId   = equipment.getId(EquipmentSlot.WEAPON)
    val magicLvl  = skills.getLevel(SkillConstants.MAGIC)    // e.g. 99
    val usesMax   = slayer.isCurrentAssignment(target) ||
            CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MAGIC)

    // if wielding the special staff (4170) OR not in one of the “always max” cases
    return if (staffId == 4170 || !usesMax) {
        floor((magicLvl / 10.0f) + 10).toDouble()   // (99/10)+10 = 19.9 → 19
    } else {
        floor((magicLvl / 6.0f)  + 13).toDouble()   // (99/6)+13 = 29.5 → 29
    }
}

/**
 * Applies the God-spell cape bonus for this player when casting [spell].
 * Returns either the flat 30 bonus or the original [baseDamage].
 */
fun Player.godSpellDamage(spell: CombatSpell, baseDamage: Int): Double {
    val capeId = equipment.getId(EquipmentSlot.CAPE)
    val validCapes = when (spell) {
        CombatSpell.CLAWS_OF_GUTHIX       -> listOf(2413, 21793, 13335, 21784)
        CombatSpell.SARADOMIN_STRIKE      -> listOf(2412, 21791, 13331, 21776)
        CombatSpell.FLAMES_OF_ZAMORAK     -> listOf(2414, 21795, 13333, 21780)
        else                              -> emptyList()
    }
    return if (capeId in validCapes) 30.0 else baseDamage.toDouble()
}