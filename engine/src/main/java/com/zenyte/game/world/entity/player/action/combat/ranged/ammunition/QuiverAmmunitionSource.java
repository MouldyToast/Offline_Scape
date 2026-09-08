package com.zenyte.game.world.entity.player.action.combat.ranged.ammunition;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

public class QuiverAmmunitionSource extends AmmunitionSource {

	public QuiverAmmunitionSource(Player player, AmmunitionDefinition ammunitionDefinition) {
		super(player, ammunitionDefinition);
		this.provideAmmo();
	}

	@Override
	public Item provideAmmoInner() {
		int itemId = PlayerAttributesKt.getDizanasQuiverAmmo(getPlayer());
		int itemAmount = PlayerAttributesKt.getDizanasQuiverAmmoAmount(getPlayer());
		if (itemId == -1 || itemAmount == 0) {
			return null;
		}

		return new Item(itemId, itemAmount);
	}

	@Override
	public void depleteAmmunition(int amount) {
		if (amount == 0) {
			return;
		}

		final Player player = getPlayer();
		final Item ammo = provideAmmoInner();//this needs to be provideammo so darkbow gets the latest value, if not it only removes 1 and not two, not an issue with container
		final int ammoAmount = ammo.getAmount();
		if (ammoAmount > amount) {
			PlayerAttributesKt.setDizanasQuiver(player, ammo.getId(), ammoAmount - amount);
		} else {
			PlayerAttributesKt.setDizanasQuiver(player, -1, 0);
		}
	}

}
