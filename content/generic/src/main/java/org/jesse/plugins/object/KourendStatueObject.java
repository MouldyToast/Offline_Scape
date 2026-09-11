package org.jesse.plugins.object;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.actions.FadeScreenAction;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.interfaces.ZeahStatueScroll;

public class KourendStatueObject implements ObjectAction {

	private static final Animation READ = new Animation(2171);
	private static final Animation CROUCH = new Animation(827);
	private static final Location ROPE = new Location(1666, 10050, 0);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.faceObject(object);

		if(option.equalsIgnoreCase("read")) {
			player.setAnimation(READ);
			WorldTasksManager.schedule(() -> { ZeahStatueScroll.open(player); }, 0);
			return;
		}

		if(option.equalsIgnoreCase("investigate")) {
			enterCatacombs(player);
		}
	}

	private void enterCatacombs(Player player) {
		player.lock();
		player.setAnimation(CROUCH);
		player.sendMessage("You investigate what looks like hinges on the plaque and find it opens.");

		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;

			@Override
			public void run() {
				if(ticks == 0) {
					new FadeScreenAction(player, 2).run();
					player.sendMessage("You climb down the hole.");
				} if(ticks == 2)
					player.setLocation(ROPE);
				else if(ticks == 3) {
					player.unlock();
					stop();
				}

				ticks++;
			}

		}, 0, 0);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { 27785 };
	}
}
