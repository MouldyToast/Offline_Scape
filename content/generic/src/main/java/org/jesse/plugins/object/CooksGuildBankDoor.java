package org.jesse.plugins.object;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.Door;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 29. mai 2018 : 15:27:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CooksGuildBankDoor implements ObjectAction {
	private static final Location OUTSIDE = new Location(3143, 3452, 0);

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final int playerPosHash = player.getLocation().getPositionHash();
		if (playerPosHash != OUTSIDE.getPositionHash() || CooksGuildEntranceDoor.hasEntranceEquipment(player)) {
			player.lock(3);
			player.addWalkSteps(object.getX(), object.getY());
			WorldTasksManager.schedule(new WorldTask() {
				private int ticks;
				private WorldObject door;
				@Override
				public void run() {
					switch (ticks++) {
					case 0: 
						door = Door.handleGraphicalDoor(object, null);
						return;
					case 1: 
						if (playerPosHash != OUTSIDE.getPositionHash()) {
							player.addWalkSteps(door.getX(), door.getY(), 1, false);
						} else {
							player.addWalkSteps(object.getX(), object.getY(), 1, false);
						}
						return;
					case 3: 
						Door.handleGraphicalDoor(door, object);
						stop();
						return;
					}
				}
			}, 0, 0);
		} else {
			World.sendClosestNPCDialogue(player, NpcId.HEAD_CHEF, new Dialogue(player) {
				@Override
				public void buildDialogue() {
					npc("Sorry. Only the finest chefs are allowed in here. You\'ll need to prove your worth to me by wearing the Cooking skillcape.");
				}
			});
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.DOOR_10045};
	}
}
