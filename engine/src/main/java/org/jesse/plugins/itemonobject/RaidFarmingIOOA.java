package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Kris | 2. mai 2018 : 21:28:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RaidFarmingIOOA implements ItemOnObjectAction {
	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<>();
		for (int i = 29997; i <= 30011; i++) {
			list.add(i);
		}
		list.add(29765);
		return list.toArray(new Object[0]);
	}

	@Override
	public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
		player.getRaid().ifPresent(raid -> {
			if (item.getId() == 952) {
				raid.getFarming().handle(player, object, "Clear");
				return;
			}
			raid.getFarming().plant(player, object, item);
		});
	}

	@Override
	public Object[] getItems() {
		return new Object[] {20903, 20906, 20909, 952};
	}
}
