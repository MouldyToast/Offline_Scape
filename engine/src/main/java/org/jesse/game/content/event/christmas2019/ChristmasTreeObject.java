package org.jesse.game.content.event.christmas2019;

import org.jesse.game.content.advent.AdventCalendarManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.area.EdgevilleArea;

import java.util.Calendar;

public class ChristmasTreeObject implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (option.equalsIgnoreCase("admire")) {
			if (!(player.getArea() instanceof EdgevilleArea)) {
				return;
			}

			Calendar cal = Calendar.getInstance();
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			if (dayOfMonth != 25) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("I wonder what this does.");
					}
				});
				return;
			}

			if (player.getSkills().getTotalLevel() < 1000) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("You need at least 1,000 total level to admire this tree.");
					}
				});
				return;
			}

			if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.EVENT_COMPLETE)) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("You need to complete the Christmas event to admire the tree.");
					}
				});
				return;
			}

			AdventCalendarManager.increaseChallengeProgress(player, 2022, 25, 1);
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{46077};
	}

}
