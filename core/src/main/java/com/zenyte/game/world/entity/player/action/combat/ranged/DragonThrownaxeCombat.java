package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.zenyte.game.content.minigame.duelarena.DuelKeys;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;

/**
 * @author Kris | 2. juuni 2018 : 04:36:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DragonThrownaxeCombat extends RangedCombat {

	public DragonThrownaxeCombat(final Entity target) {
		super(target);
	}

	@Override
	protected void dropAmmunition(final int delay, final boolean destroy) {
		if (ammunitionSource == null) {
			return;
		}

		final int dropChance = getAmmunitionDropChance();
		final int roll = Utils.random(100);
		final Equipment equipment = player.getEquipment();
		final boolean destroyAmmo = (ammunitionSource.getAmmunitionDefinition() == AmmunitionDefinitions.DRAGON_THROWNAXE && player.getTemporaryAttributes().get("dragonThrownaxe") != null);
		if (destroyAmmo || destroy || roll <= BREAK_CHANCE || roll <= (BREAK_CHANCE + dropChance)) {
			ammunitionSource.depleteAmmunition(1);
			if (destroy || roll < BREAK_CHANCE) {
				return;
			}
		}
		if (roll <= (BREAK_CHANCE + dropChance)) {
			final Location location = new Location(target.getLocation());
			final Item item = new Item(ammunitionSource.getAmmo().getId());
			WorldTasksManager.schedule(() -> {
				final Duel duel = DuelKeys.getDuel(player);
				if (duel != null) {
					duel.getAmmunitions().get(player).add(item);
				} else {
					World.spawnFloorItem(item, !World.isFloorFree(location, 1) ? new Location(player.getLocation()) : location, 20, player, player, 300, 500);
				}
			}, delay);
		}
	}
}
