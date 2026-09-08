package com.zenyte.game.world.entity.player.action.combat.ranged.ammunition;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

public abstract class AmmunitionSource {

	private final Player player;
	private final AmmunitionDefinition ammunitionDefinition;
	private Item ammo;

	public AmmunitionSource(Player player, AmmunitionDefinition ammunitionDefinition) {
		this.player = player;
		this.ammunitionDefinition = ammunitionDefinition;
	}

	public void provideAmmo() {
		this.ammo = provideAmmoInner();
	}

	public abstract Item provideAmmoInner();

	public abstract void depleteAmmunition(int amount);

	public Player getPlayer() {
		return player;
	}

	public AmmunitionDefinition getAmmunitionDefinition() {
		return ammunitionDefinition;
	}

	public Item getAmmo() {
		return ammo;
	}

	public boolean runDegrade() {
		return true;
	}

}
