package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

import static org.jesse.game.npc.ids.NpcId.DHAROK_THE_WRETCHED_12447;

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
