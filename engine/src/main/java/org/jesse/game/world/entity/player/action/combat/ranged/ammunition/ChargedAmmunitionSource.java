package org.jesse.game.world.entity.player.action.combat.ranged.ammunition;

import org.jesse.game.world.entity.player.action.combat.AmmunitionDefinition;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;

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
