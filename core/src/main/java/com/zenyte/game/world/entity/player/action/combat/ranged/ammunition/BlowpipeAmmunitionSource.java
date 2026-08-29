package com.zenyte.game.world.entity.player.action.combat.ranged.ammunition;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public class BlowpipeAmmunitionSource extends AmmunitionSource {

	public BlowpipeAmmunitionSource(Player player, AmmunitionDefinition ammunitionDefinition) {
		super(player, ammunitionDefinition);
		this.provideAmmo();
	}

	@Override
	public Item provideAmmoInner() {
		final Player player = getPlayer();
		final Item weapon = player.getWeapon();
		return new Item(weapon.getNumericAttribute("blowpipeDartType").intValue(), weapon.getNumericAttribute("blowpipeDarts").intValue());
	}

	@Override
	public void depleteAmmunition(int amount) {
		if (amount == 1) {
			amount = 2;
		} else {
			amount = 1;
		}

		final Player player = getPlayer();
		final Item blowpipe = player.getWeapon();
		player.getChargesManager().removeCharges(blowpipe, amount, player.getEquipment().getContainer(), EquipmentSlot.WEAPON.getSlot());
	}

	@Override
	public boolean runDegrade() {
		return false;
	}

}
