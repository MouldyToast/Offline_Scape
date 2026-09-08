package org.jesse.game.content.area.prifddinas.zalcano;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;

@SuppressWarnings("unused")
public class ZalcanoGolemDrop extends DropProcessor {

	@Override
	public void attach() {
		appendDrop(new DisplayedDrop(ItemId.IMBUED_TEPHRA, 16, 24, 1));
	}

	@Override
	public void onDeath(NPC npc, Player killer) {
		npc.dropItem(killer, new Item(ItemId.IMBUED_TEPHRA, Utils.random(16, 24)));
		killer.getCombatAchievements().complete(CAType.TEAM_PLAYER);
	}

	@Override
	public int[] ids() {
		return new int[] {ZalcanoConstants.ZALCANO_GOLEM};
	}

}
