package org.jesse.game.world.entity.player.action.combat.ranged;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.entity.player.action.combat.RangedCombat;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.region.RegionArea;
import org.jesse.game.world.region.area.plugins.PlayerCombatPlugin;

import static org.jesse.game.world.entity.player.action.combat.CombatUtilities.KARILS_SET_GFX;

/**
 * @author Kris | 1. juuni 2018 : 23:18:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class KarilsRangedCombat extends RangedCombat {
	public KarilsRangedCombat(final Entity target) {
		super(target);
	}

	@Override
	public int processAfterMovement() {
		if (!isWithinAttackDistance()) {
			return 0;
		}
		if (!canAttack()) {
			return -1;
		}
		addAttackedByDelay(player, target);
		final RegionArea area = player.getArea();
		if (area instanceof PlayerCombatPlugin) {
			((PlayerCombatPlugin) area).onAttack(player, target, "Ranged", null, false);
		}
		final int ticks = this.fireProjectile();
		if (ammunitionSource.getAmmunitionDefinition().getSoundEffect() != null) {
			player.getPacketDispatcher().sendSoundEffect(ammunitionSource.getAmmunitionDefinition().getSoundEffect());
		}
		animate();
		dropAmmunition(ticks, !ammunitionSource.getAmmunitionDefinition().isRetrievable());
		resetFlag();
		final Hit hit = getHit(player, target, 1, 1, 1, false);
		if (hit.getDamage() > 0) {
			addPoisonTask(ticks);
		}
		extra(hit);
		delayHit(ticks, hit);
		final boolean hasFullSet = CombatUtilities.hasFullBarrowsSet(player, "Karil's");
		final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
		if ((amuletId == 12851 || amuletId == 12853) && hasFullSet && Utils.random(3) == 0) {
			final Hit secondHit = new Hit(player, (int) (hit.getDamage() / 2.0F), hit.getHitType());
			delayHit(ticks, secondHit);
		}
		if (hit.getDamage() > 0 && Utils.random(3) == 0 && hasFullSet) {
			WorldTasksManager.schedule(() -> {
				target.setGraphics(KARILS_SET_GFX);
				target.drainSkill(SkillConstants.AGILITY, 20.0);
			});
		}
		drawback();
		checkIfShouldTerminate(HitType.RANGED);
		return getWeaponSpeed();
	}
}
