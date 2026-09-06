package com.zenyte.plugins.interfaces;

import com.zenyte.game.content.skills.construction.ConstructionKeys;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 23. nov 2017 : 3:47.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HouseOptionsTab implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (player.isLocked()) {
			return;
		}
		switch(componentId) {
		case 1:
			ConstructionKeys.construction(player).getHouseViewer().openHouseViewer();
			return;
		case 5:
			ConstructionKeys.construction(player).setBuildingMode(true);
			break;
		case 6:
			ConstructionKeys.construction(player).setBuildingMode(false);
			break;
		case 8:
			ConstructionKeys.construction(player).setRenderDoorsOpen(true);
			break;
		case 9:
			ConstructionKeys.construction(player).setRenderDoorsOpen(false);
			break;
		case 11:
			ConstructionKeys.construction(player).setTeleportInside(true);
			break;
		case 12:
			ConstructionKeys.construction(player).setTeleportInside(false);
			break;
		}
		ConstructionKeys.construction(player).refreshHouseOptions();
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 370 };
	}

}
