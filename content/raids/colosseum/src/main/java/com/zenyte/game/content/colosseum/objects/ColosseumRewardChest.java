package com.zenyte.game.content.colosseum.objects;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.colosseum.ColosseumInstance;
import com.zenyte.game.content.colosseum.ColosseumRewardChestInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RegionArea;

@SuppressWarnings("unused")
public class ColosseumRewardChest implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		RegionArea instance = player.getArea();
		if (!(instance instanceof ColosseumInstance colosseum)) {
			return;
		}

		Container container = colosseum.getRewards();
		if (container.isEmpty()) {
			player.sendMessage("Your reward chest is empty.");
			return;
		}

		for (final Item item : container.getItems().values()) {
			player.getCollectionLog().add(item);
		}

		player.getPacketDispatcher().sendUpdateItemContainer(container);
		GameInterface.COLOSSEUM_REWARDS.open(player);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.COLOSSEUM_REWARDS, 11, 0, Container.getSize(ContainerType.COLOSSEUM_REWARDS), AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
		player.getPacketDispatcher().sendClientScript(150, GameInterface.COLOSSEUM_REWARDS.getId() << 16 | 11, ContainerType.COLOSSEUM_REWARDS.getId(), 3, 5, 0, -1, "Take", "", "", "", "", "", "", "", "");
		ColosseumRewardChestInterface.setValue(player, container);
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.REWARDS_CHEST_50741};
	}

}
