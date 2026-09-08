package org.jesse.plugins.flooritem;

import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.flooritem.FloorItem;

/**
 * @author Tommeh | 29 mei 2018 | 20:05:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodCapeTakePlugin implements FloorItemPlugin {
	
	private static final Animation TAKE_ANIM = new Animation(832);

	@Override
	public void handle(Player player, FloorItem item, int optionId, String option) {
		if (option.equals("Take")) {
		    if (item.hasOwner()) {
                World.takeFloorItem(player, item);
		        return;
            }
			if (player.getInventory().addItem(item).getResult().equals(RequestResult.NOT_ENOUGH_SPACE)) {
				player.sendFilteredMessage("Not enough space in your inventory.");
				return;
			}
			player.setAnimation(TAKE_ANIM);
			return;
		}
	}
	
	@Override
	public boolean overrideTake() {
		return true;
	}

	@Override
	public int[] getItems() {
		return new int[] { 2412, 2413, 2414};
	}

}
