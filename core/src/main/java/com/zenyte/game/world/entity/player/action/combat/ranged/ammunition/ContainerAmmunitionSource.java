package com.zenyte.game.world.entity.player.action.combat.ranged.ammunition;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public class ContainerAmmunitionSource extends AmmunitionSource {

	private final EquipmentSlot slot;

	public ContainerAmmunitionSource(Player player, AmmunitionDefinition ammunitionDefinition) {
		super(player, ammunitionDefinition);
		this.slot = ammunitionDefinition.isWeapon() ? EquipmentSlot.WEAPON : EquipmentSlot.AMMUNITION;
		this.provideAmmo();
	}

	@Override
	public Item provideAmmoInner() {
		return getPlayer().getEquipment().getItem(slot);
	}

	@Override
	public void depleteAmmunition(int amount) {
		if (amount == 0) {
			return;
		}

		final Player player = getPlayer();
		final Equipment equipment = player.getEquipment();
		final Item ammo = getAmmo();
		final int ammoAmount = ammo.getAmount();
		if (ammoAmount > amount) {
			ammo.setAmount(ammoAmount - amount);
		} else {
			equipment.set(slot, null);
			if (slot == EquipmentSlot.WEAPON) {//Only refresh if you ran out of weapon type ammunition
				player.getCombatDefinitions().refresh();
			}
		}
		equipment.refresh(slot.getSlot());
	}

}
