package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

import static com.zenyte.game.world.entity.npc.NpcId.SULPHUR_LIZARD_12458;

public class SulphurLizard extends BreachEntity implements Spawnable, CombatScript {


	public SulphurLizard(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		animate();
		executeMeleeHit(target, CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target));
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == SULPHUR_LIZARD_12458;
	}

}
