package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.action.combat.*;
import com.zenyte.game.world.entity.player.action.combat.ranged.ammunition.BlowpipeAmmunitionSource;

/**
 * @author Kris | 1. juuni 2018 : 03:34:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BlowpipeRangedCombat extends RangedCombat {

	public BlowpipeRangedCombat(final Entity target) {
		super(target);
	}

	@Override
	public int getBaseAttackSpeed() {
		return isPvp() ? 4 : 3;
	}

	@Override
	protected void addPoisonTask(final int delay) {
		if (target instanceof NPC && CombatUtilities.isWearingSerpentineHelmet(player) || Utils.randomBoolean(3)) {
			WorldTasksManager.schedule(() -> target.getToxins().applyToxin(ToxinType.VENOM, 6, player), delay);
		}
	}

	@Override
	public boolean applyAmmo() {
		final Item item = player.getWeapon();
		if (item == null) {
			return false;
		}
		if (item.getNumericAttribute("blowpipeDarts").intValue() == 0) {
			player.sendMessage("You need to charge your blowpipe with some darts first.");
			return false;
		}
		if (item.getNumericAttribute("blowpipeScales").intValue() == 0) {
			player.sendMessage("You need to charge your blowpipe with some scales first.");
			return false;
		}

		final int dartId = item.getNumericAttribute("blowpipeDartType").intValue();
		ammunitionSource = new BlowpipeAmmunitionSource(player, AmmunitionDefinitions.getBlowpipeDefinitions(dartId));
		return true;
	}

}
