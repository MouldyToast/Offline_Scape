package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

import static com.zenyte.game.world.entity.npc.NpcId.DHAROK_THE_WRETCHED_12447;

public class DharokTheWretched extends BreachEntity implements Spawnable, CombatScript {


	public DharokTheWretched(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		final int maxHealth = getMaxHitpoints();
		final int health = getHitpoints();
		final int max = (int) (29.0F + (29.0F * ((float) (maxHealth - health) / maxHealth)));
		animate();
		executeMeleeHit(target, max);
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == DHAROK_THE_WRETCHED_12447;
	}

}
