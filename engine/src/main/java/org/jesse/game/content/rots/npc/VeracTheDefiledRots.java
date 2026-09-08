package org.jesse.game.content.rots.npc;

import org.jesse.game.content.rots.RotsInstance;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;

public class VeracTheDefiledRots extends RotsBrother implements CombatScript {

	private static final Graphics VERACS_GFX = new Graphics(1041);

	public VeracTheDefiledRots(final Location tile, RotsInstance instance) {
		super(16040, tile, instance);
	}

	@Override
	public int attack(final Entity target) {
		HitType type = HitType.MELEE;
		int max = combatDefinitions.getMaxHit();
		if (Utils.random(3) == 0) {
			target.setGraphics(VERACS_GFX);
			if (target instanceof final Player player) {
				type = HitType.DEFAULT;
				if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
					max *= 0.667F;
				}
			}
			animate();
			final Hit hit = new Hit(this, Utils.random(max), type);
			delayHit(0, target, hit);
			return combatDefinitions.getAttackSpeed();
		}
		if (Utils.randomBoolean(11) && target instanceof Player player) {
			if (player.getPrayerManager().getActivePrayers().size() > 0) {
				player.getPrayerManager().deactivateActivePrayers();
				setForceTalk("Where is your god now?");
			}
		}
		animate();
		executeMeleeHit(target, type, max);
		return combatDefinitions.getAttackSpeed();
	}

}
