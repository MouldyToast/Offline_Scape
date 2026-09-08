package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.FillContainer;
import org.jesse.game.world.object.FillContainer.FillCombinations;
import org.jesse.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 11. nov 2017 : 1:01.38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class VesselFilling implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		FillContainer.tryAction(player, object, item);
	}

	@Override
	public Object[] getItems() {
		final List<Object> list = new ArrayList<Object>();
		FillCombinations.combos.forEach((k, v) -> list.addAll(v.getEmpty()));
		return list.toArray(new Object[0]);
	}

	@Override
	public Object[] getObjects() {
		final List<Object> list = new ArrayList<Object>();
		FillCombinations.combos.forEach((k, v) -> list.add(k));
		return list.toArray(new Object[0]);
	}

}
