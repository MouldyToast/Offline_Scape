package org.jesse.game.content.boss.nightmare.object;

import org.jesse.game.content.boss.nightmare.area.NightmareBossArea;
import org.jesse.game.content.boss.nightmare.area.PhosaniInstance;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class NightmareEnergyBarrier implements ObjectAction {

	private static Location LEAVE_LOCATION = new Location(3808, 9755, 1);
	private static Location LEAVE_LOCATION_PHOSANIS = new Location(3808, 9779, 1);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Are you sure you want to leave?", new DialogueOption("Yes.", () -> {
					player.lock();
					player.getPacketDispatcher().sendClientScript(2893, 41549825, 41549826, 39504, 13109328, -1, -1);
					player.blockIncomingHits(6);
					WorldTasksManager.schedule(() -> {
						player.unlock();
						if (player.getArea() instanceof PhosaniInstance) {
							player.teleport(LEAVE_LOCATION_PHOSANIS);
						} else {
							player.teleport(LEAVE_LOCATION);
						}
						player.setAnimation(NightmareBossArea.ENTER_ANIMATION_END);
						player.getPacketDispatcher().sendClientScript(2894, 41549825, 41549826, -1, -1);
					}, 2);
				}), new DialogueOption("No."));
			}
		});
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.ENERGY_BARRIER_37730};
	}

}
