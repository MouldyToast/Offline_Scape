package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.PotteryShapingD;

public class PottersWheel implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.getDialogueManager().start(new PotteryShapingD(player));
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { ObjectId.POTTERS_WHEEL };
	}

}
