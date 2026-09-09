package org.jesse.game.content.minigame.barrows.wights;

import org.jesse.game.content.minigame.barrows.BarrowsWightNPC;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.variables.PlayerVariables;


/**
 * @author Kris | 29. sept 2018 : 05:39:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class ToragTheCorrupted extends BarrowsWightNPC implements Spawnable, CombatScript {
	private static final Graphics TORAGS_GFX = new Graphics(399);

	public ToragTheCorrupted(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
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

	@Override
	public boolean validate(final int id, final String name) {
		switch (id) {
			case 1676:
			case 16056: {
				return true;
			}
			default:
				return false;
		}
	}

}
