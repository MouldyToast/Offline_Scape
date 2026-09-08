package org.jesse.game.world.entity.player.action.combat.melee;

import org.jesse.game.model.item.degradableitems.DegradeType;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.CombatDefinitions;
import org.jesse.game.world.entity.player.action.combat.MeleeCombat;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.action.combat.SpecialType;
import org.jesse.game.world.region.RegionArea;
import org.jesse.game.world.region.area.plugins.PlayerCombatPlugin;

/**
 * @author Kris | 2. juuni 2018 : 22:50:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class GraniteMaulCombat extends MeleeCombat {
	public GraniteMaulCombat(final Entity target) {
		super(target);
	}

	@Override
	public boolean postMovementProcess() {
		final boolean process = super.process();
		if (!process) return false;
		if (!player.wieldingGraniteMaul()) return false;
		final int queuedSpecCount = specCount();
		if (queuedSpecCount <= 0 || !inAttackRange()) return false;
		instantSpec(queuedSpecCount);
		player.getTemporaryAttributes().remove(PlayerCombat.QUEUED_SPECS_ATTRIBUTE);
		return true;
	}

	private boolean inAttackRange() {
		if (!isWithinAttackDistance()) {
			return false;
		}
		return canAttack();
	}

	private int specCount() {
		final Object attribute = player.getTemporaryAttributes().get(PlayerCombat.QUEUED_SPECS_ATTRIBUTE);
		final int cachedMaulSpecials = (attribute instanceof Integer ? (Integer) attribute : 0);
		return Math.min(cachedMaulSpecials, 2);
	}

	private void instantSpec(final int queuedSpecCount) {
		final CombatDefinitions combatDefinitions = player.getCombatDefinitions();
		for (int i = 0; i < queuedSpecCount; i++) {
			combatDefinitions.setUsingSpecial(true);
			if (combatDefinitions.getSpecialEnergy() < getRequiredSpecial(player)) {
				continue;
			}
			useSpecial(player, SpecialType.MELEE);
		}
		combatDefinitions.setUsingSpecial(false);
		combatDefinitions.refresh();
	}

	private int attack() {
		sendSoundEffect();
		final Hit hit = getHit(player, target, 1, 1, 1, false);
		extra(hit);
		addPoisonTask(hit.getDamage(), 0);
		delayHit(0, hit);
		animate();
		player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
		resetFlag();
		checkIfShouldTerminate(HitType.MELEE);
		return getSpeed();
	}

	@Override
	public int processAfterMovement() {
		if (!isWithinAttackDistance()) {
			return 0;
		}
		if (!canAttack()) {
			return -1;
		}
		final RegionArea area = player.getArea();
		if (area instanceof PlayerCombatPlugin) {
			((PlayerCombatPlugin) area).onAttack(player, target, "Melee", null, false);
		}
		addAttackedByDelay(player, target);
		return attack();
	}
}
