package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.tombsofamascut.encounter.ScabarasEncounter;
import org.jesse.game.content.tombsofamascut.raid.ScabarasPuzzleType;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class AncientTabletAction implements ObjectAction {

	private static final SoundEffect SOUND_EFFECT = new SoundEffect(6548);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof final ScabarasEncounter encounter &&
				encounter.canUse(player, ScabarasPuzzleType.SUM, "You can't seem to make out the writing. Weird.")) {
			player.sendMessage("The number <col=ef1020>" + encounter.getSumGoal() + "</col> has been hastily chipped into the stone.");
			player.sendSound(SOUND_EFFECT);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {ScabarasEncounter.SUM_TABLET_ID};
	}
}
