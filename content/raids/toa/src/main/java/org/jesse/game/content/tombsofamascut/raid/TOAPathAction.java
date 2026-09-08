package org.jesse.game.content.tombsofamascut.raid;

import org.jesse.game.content.tombsofamascut.encounter.MainHallEncounter;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.RegionArea;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Savions.
 */
public class TOAPathAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final RegionArea area = GlobalAreaManager.getArea(player);
		if (area instanceof MainHallEncounter encounter) {
			final boolean quickEnter = !"Enter".equalsIgnoreCase(option);
			if (object.getId() == 46168) {
				encounter.handleWardensEnter(player, quickEnter);
			} else {
				final Optional<TOAPathType> optional = Arrays.stream(TOAPathType.VALUES).
						filter(path -> encounter.getLocation(path.getEntranceLocation()).equals(object.getLocation())).findFirst();
				optional.ifPresent(pathType -> encounter.handlePathEnter(pathType, player, quickEnter));
			}
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {46161, 46162, 46155, 46156, 46158, 46159, 46164, 46165, 46168};
	}
}
