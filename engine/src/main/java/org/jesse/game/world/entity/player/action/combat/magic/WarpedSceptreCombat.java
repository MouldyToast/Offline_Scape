package org.jesse.game.world.entity.player.action.combat.magic;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.degradableitems.DegradableItem;
import org.jesse.game.model.item.degradableitems.DegradeType;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.action.combat.MagicCombat;

/**
 * @author Zei
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/18/2025
 */
public final class WarpedSceptreCombat extends MagicCombat {
	public WarpedSceptreCombat(final Entity target, final CombatSpell spell, final CastType type) {
		super(target, spell, type);
	}

	@Override
	protected int baseDamage() {
		return (int) Math.max(24, Math.floor((8 * player.getSkills().getLevel(SkillConstants.MAGIC) + 96) / 37.0F));
	}

	@Override
	protected void extra(Hit hit) {
		super.extra(hit);
		final Item weapon = player.getWeapon();
		if (weapon == null || weapon.getCharges() <= 0) {
			interrupt = true;
		}
	}

	@Override
	protected int attackSpeed() {
		return 4;
	}

	@Override
	protected boolean canAttack() {
		final Item weapon = player.getWeapon();
		final int charges = weapon.getCharges();
		if (DegradableItem.getDefaultCharges(weapon.getId(), -1) != charges && charges <= 0) {
			player.sendMessage("Your sceptre is out of charges.");
			return false;
		}
		return super.canAttack();
	}

	@Override
	protected int getAttackDistance() {
		if (player.getCombatDefinitions().getStyle() == 3) {
			return 9;
		}
		return 7;
	}

	@Override
	protected void degrade() {
		player.getChargesManager().removeCharges(DegradeType.TRIDENT);
	}
}
