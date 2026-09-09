package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.smithing.SpiritShieldCreationAction;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.BossDropItem;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

public class SpiritShieldCreationPlugin implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		final BossDropItem shield = BossDropItem.getItemByMaterials(item, SpiritShieldCreationAction.BLESSED_SPIRIT_SHIELD);
		player.getActionManager().setAction(new SpiritShieldCreationAction(shield));
	}

	@Override
	public Object[] getItems() {
		return new Object[] {12819, 12823, 12827};
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Anvil"};
	}
}
