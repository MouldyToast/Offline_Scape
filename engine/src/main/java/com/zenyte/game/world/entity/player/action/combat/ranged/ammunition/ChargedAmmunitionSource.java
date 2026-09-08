package com.zenyte.game.world.entity.player.action.combat.ranged.ammunition;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

public class ChargedAmmunitionSource extends AmmunitionSource {

	public ChargedAmmunitionSource(Player player, AmmunitionDefinition ammunitionDefinition) {
		super(player, ammunitionDefinition);
	}

	@Override
	public Item provideAmmoInner() {
		return null;
	}

	@Override
	public void depleteAmmunition(int amount) {

	}

}
