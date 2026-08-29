package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;

public class MorriganBHWeaponsCombat extends RangedCombat {

	public MorriganBHWeaponsCombat(Entity target) {
		super(target);
	}

	@Override
	public void dropAmmunition(int delay, boolean destroy) {
		/* empty */
	}

}