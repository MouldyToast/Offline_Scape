package org.jesse.game.content.tombsofamascut.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.plugins.equipment.equip.EquipPlugin;

@SuppressWarnings("unused")
public class LightbearerPlugin implements EquipPlugin {

	@Override
	public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
		return true;
	}

	@Override
	public void onEquip(Player player, Container container, Item equippedItem) {
		player.getVariables().setSpecRegeneration(0);
	}

	@Override
	public void onUnequip(Player player, Container container, Item unequippedItem) {
		player.getVariables().setSpecRegeneration(0);
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.LIGHTBEARER};
	}

}
