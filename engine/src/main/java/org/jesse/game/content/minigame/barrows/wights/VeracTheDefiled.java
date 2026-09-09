package org.jesse.game.content.minigame.barrows.wights;

import org.jesse.game.content.minigame.barrows.BarrowsWightNPC;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;


/**
 * @author Kris | 29. sept 2018 : 05:19:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class VeracTheDefiled extends BarrowsWightNPC implements Spawnable, CombatScript {
	private static final Graphics VERACS_GFX = new Graphics(1041);

	public VeracTheDefiled(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		HitType type = HitType.MELEE;
		int max = combatDefinitions.getMaxHit();
		if (Utils.random(3) == 0) {
			target.setGraphics(VERACS_GFX);
			if (target instanceof Player) {
				type = HitType.DEFAULT;
				final Player player = (Player) target;
				if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
					max *= 0.667F;
				}
			}
			animate();
			final Hit hit = new Hit(this, Utils.random(max), type);
			delayHit(0, target, hit);
			return combatDefinitions.getAttackSpeed();
		}
		animate();
		executeMeleeHit(target, type, max);
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		switch (id) {
			case 1677:
			case 16057: {
				return true;
			}
			default:
				return false;
		}
	}

}
