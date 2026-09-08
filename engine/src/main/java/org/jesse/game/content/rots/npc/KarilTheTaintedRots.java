package org.jesse.game.content.rots.npc;

import org.jesse.game.content.rots.RotsInstance;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.SkillConstants;

public class KarilTheTaintedRots extends RotsBrother implements CombatScript {

	private static final Graphics GFX = new Graphics(401, 0, 96);
	private static final Projectile PROJ = new Projectile(27, 42, 30, 40, 15, 3, 64, 5);
	private int attacksLeft = 0;

	public KarilTheTaintedRots(final Location tile, RotsInstance instance) {
		super(16038, tile, instance);
	}

	@Override
	public int attack(final Entity target) {
		if (Utils.randomBoolean(11)) {
			attacksLeft = 10;
			setForceTalk("MuHaHA rapid fire incoming.");
		}

		animate();
		this.delayHit(World.sendProjectile(this, target, PROJ), target, ranged(target, combatDefinitions.getMaxHit()).onLand(hit -> {
			if (hit.getDamage() > 0) {
				if (Utils.random(3) == 0) {
					target.setGraphics(GFX);
					target.drainSkill(SkillConstants.AGILITY, 20F);
				}
			}
		}));

		int attackSpeed = combatDefinitions.getAttackSpeed();
		if (attacksLeft > 0) {
			attacksLeft--;
			attackSpeed /= 2;
		}

		return attackSpeed;
	}

}
