package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

import static org.jesse.game.npc.ids.NpcId.SULPHUR_LIZARD_12458;

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
