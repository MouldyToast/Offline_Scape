package org.jesse.game.content.rots.npc;

import org.jesse.game.content.rots.RotsInstance;
import org.jesse.game.util.Utils;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.variables.PlayerVariables;

public class ToragTheCorruptedRots extends RotsBrother implements CombatScript {

	private static final Graphics TORAGS_GFX = new Graphics(399);
	private long reflectTicks;
	public ToragTheCorruptedRots(final Location tile, RotsInstance instance) {
		super(16039, tile, instance);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);

		if (reflectTicks >= WorldThread.getCurrentCycle() && hit.getDamage() > 0 && hit.getSource() instanceof Player player) {
			player.scheduleHit(this, hit, 0);
		}
	}

	@Override
	public int attack(final Entity target) {
		if (Utils.randomBoolean(11)) {
			reflectTicks = WorldThread.getCurrentCycle() + 10;
			setGraphics(new Graphics(1517, 0, 300));
		}

		this.executeMeleeHit(target, combatDefinitions.getMaxHit()).onLand(hit -> {
			if (hit.getDamage() > 0 && Utils.random(3) == 0) {
				target.setGraphics(TORAGS_GFX);
				if (target instanceof Player) {
					final PlayerVariables variables = ((Player) target).getVariables();
					double energy = variables.getRunEnergy();
					energy -= (energy * 0.2F);
					variables.setRunEnergy(energy);
				}
			}
		});
		animate();
		return combatDefinitions.getAttackSpeed();
	}

}
