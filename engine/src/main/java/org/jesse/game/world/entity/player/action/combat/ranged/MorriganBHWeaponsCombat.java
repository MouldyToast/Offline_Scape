package org.jesse.game.world.entity.player.action.combat.ranged;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.player.action.combat.RangedCombat;

public class MorriganBHWeaponsCombat extends RangedCombat {

	public MorriganBHWeaponsCombat(Entity target) {
		super(target);
	}

	@Override
	public void dropAmmunition(int delay, boolean destroy) {
		/* empty */
	}

}