package org.jesse.plugins.drop;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.runelite.api.ItemID;

public class VyreSentinelsDropProcessor extends DropProcessor {

	@Override
	public void attach() {
		appendDrop(new DropProcessor.DisplayedDrop(ItemId.BLOOD_SHARD, 1, 1, 600));
	}

	@Override
	public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
		if (Utils.random(600) == 0) {
			npc.dropItem(killer, new Item(ItemID.BLOOD_SHARD, 1));
		}
		return item;
	}

	@Override
	public int[] ids() {
		ObjectArrayList<Integer> ids = new ObjectArrayList<>();
		for (int i = 9756; i <= 9763; i++) {
			ids.add(i);
		}

		return ids.stream().mapToInt(i -> i).toArray();
	}
}
