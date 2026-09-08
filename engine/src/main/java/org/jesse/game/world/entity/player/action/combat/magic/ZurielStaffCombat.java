package org.jesse.game.world.entity.player.action.combat.magic;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.player.action.combat.MagicCombat;

public class ZurielStaffCombat extends MagicCombat {

	public ZurielStaffCombat(final Entity target, final CombatSpell spell, final CastType castType) {
		super(target, spell, castType);
	}

	@Override
	protected int attackSpeed() {
		if (spell.getSpellbook().equals(Spellbook.NORMAL)) {
			return 4;
		}
		return 5;
	}

}
