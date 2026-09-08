package org.jesse.game.world.entity.player.action.combat.melee;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.entity.player.action.combat.MeleeCombat;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;

import static org.jesse.game.world.entity.player.action.combat.CombatUtilities.GUTHANS_SET_GFX;

/**
 * @author Kris | 2. juuni 2018 : 22:46:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GuthanCombat extends MeleeCombat {
	public GuthanCombat(final Entity target) {
		super(target);
	}

	@Override
	protected void extra(final Hit hit) {
		super.extra(hit);
		if (Utils.random(3) != 0 || !CombatUtilities.hasFullBarrowsSet(player, "Guthan's")) return;
		target.setGraphics(GUTHANS_SET_GFX);
		final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
		final int amount = hit.getDamage();
		if (amuletId == 12851 || amuletId == 12853) {
			final int hitpoints = player.getHitpoints();
			if (hitpoints < (player.getMaxHitpoints() + 10) + amount) {
				player.setHitpoints(Math.min((hitpoints + amount), (player.getMaxHitpoints() + 10)));
			}
		} else {
			player.heal(amount);
		}
	}
}
