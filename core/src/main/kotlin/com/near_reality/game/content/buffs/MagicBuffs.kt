package com.near_reality.game.content.buffs

import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell
import com.zenyte.game.world.entity.player.variables.TickVariable

object MagicBuffs {
    val magicDartDamage = Buff(
        id = "magic_dart_dmg",
        name = "Magic dart damage",
        category = BuffCategory.MAGIC,
        subcategory = BuffSubcategory.MAGIC_DAMAGE_OVERWRITE,
        exclusive = true,
        predicate = { player -> player.spell() == CombatSpell.MAGIC_DART },
        modify = { player, damage -> player.target()?.let { player.magicDartDamage(it) } ?: damage }
    )

    val chargeSpellUsage = Buff(
        id = "charge_spell_usage",
        name = "Charge spell usage",
        category = BuffCategory.MAGIC,
        subcategory = BuffSubcategory.MAGIC_DAMAGE_OVERWRITE,
        exclusive = true,
        predicate = { player -> player.variables.getTime(TickVariable.CHARGE) > 0 },
        modify = { player, damage -> player.spell()?.let { player.godSpellDamage(it, damage.toInt()) } ?: damage }
    )

    init {
        PlayerBuffManager.register(magicDartDamage)
        PlayerBuffManager.register(chargeSpellUsage)
    }


}