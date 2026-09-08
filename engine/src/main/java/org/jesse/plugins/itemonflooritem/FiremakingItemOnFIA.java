package org.jesse.plugins.itemonflooritem;

import org.jesse.game.content.skills.firemaking.Firemaking;
import org.jesse.game.content.skills.firemaking.FiremakingAction;
import org.jesse.game.content.skills.firemaking.FiremakingTool;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnFloorItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.flooritem.FloorItem;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Kris | 11. mai 2018 : 15:12:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FiremakingItemOnFIA implements ItemOnFloorItemAction {
	@Override
	public void handleItemOnFloorItemAction(final Player player, final Item item, final FloorItem floorItem) {
		final Firemaking val = Firemaking.MAP.get(floorItem.getId());
		if (val == null) {
			return;
		}
		final Optional<FiremakingTool> tool = FiremakingTool.getTool(item.getId());
		if (!tool.isPresent()) {
			player.sendMessage("You cannot light a fire without the proper tool.");
			return;
		}
		player.getActionManager().setAction(new FiremakingAction(val, -1, true, tool.get(), Optional.of(floorItem)));
	}

	@Override
	public Object[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final FiremakingTool tool : FiremakingTool.values) {
			for (final int id : tool.getBowIds()) list.add(id);
		}
		return list.toArray();
	}

	@Override
	public Object[] getFloorItems() {
		final ArrayList<Integer> list = new ArrayList<Integer>(Firemaking.VALUES.length);
		for (final Firemaking fm : Firemaking.VALUES) {
			list.add(fm.getLogs().getId());
		}
		return list.toArray(new Object[list.size()]);
	}
}
