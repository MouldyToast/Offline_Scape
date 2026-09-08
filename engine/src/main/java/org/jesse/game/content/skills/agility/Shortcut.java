package org.jesse.game.content.skills.agility;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.logger.NearRealityPrintStream;

import java.util.function.Consumer;

/**
 * @author Kris | 14. apr 2018 : 22:25.28
 * @author Jire
 */
public interface Shortcut extends Obstacle, ObjectAction {

	@Override
	default void schedule(final Player player, final WorldObject object, final boolean success, final double additionalXp, final Consumer<Player> onComplete) {
		if (success) {
			startSuccess(player, object);
		} else {
			((Failable) this).startFail(player, object);
		}
		if (getDuration(success, object) != -1) {
			WorldTasksManager.schedule(() -> {
				finish(player, object, additionalXp, success, onComplete);
				if (success) {
					endSuccess(player, object);
				} else {
					((Failable) Shortcut.this).endFail(player, object);
				}
			}, getDuration(success, object));
		}
	}

	@Override
	default void handle(final Player player, final WorldObject object, final double additionalXp, final Consumer<Player> onComplete) {
		final boolean success = AgilityCourseManager.calculateSuccess(player, object, this);
		final Location event = getRouteEvent(player, object);
		if (event == null) {
			return;
		}
		final Runnable runnable = () -> {
			player.stopAll();
			player.faceObject(object);
			if (player.getSkills().getLevel(SkillConstants.AGILITY) < getLevel(object)) {
				player.sendMessage("You need an Agility level of at least " + getLevel(object) + " to use this Agility shortcut.");
				return;
			}
			if (!preconditions(player, object)) {
				return;
			}
			player.lock();
			player.addFreezeImmunity(getDelay());
			player.getTemporaryAttributes().put("courseRun", player.isRun());
			player.setRunSilent(true);
			if (getRenderAnimation() != null) {
				player.getAppearance().setRenderAnimation(getRenderAnimation());
			}
			if (getStartMessage(success) != null) {
				player.sendMessage(getStartMessage(success));
			} else if (getFilterableStartMessage(success) != null) {
				player.sendFilteredMessage(getFilterableEndMessage(success));
			}
			try {
				schedule(player, object, success, additionalXp, onComplete);
			} catch (Exception e) {
				e.printStackTrace(NearRealityPrintStream.getErrorStream());
				player.unlock();
			}
		};
		if (event instanceof WorldObject) {
			player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy((WorldObject) event, distance(object)), runnable, 1));
		} else {
			player.setRouteEvent(new TileEvent(player, new TileStrategy(event, distance(object)), runnable, 1));
		}
	}

	@Override
	default void finish(final Player player, final WorldObject object, final double additionalXp, final boolean success, final Consumer<Player> onComplete) {
		player.setRunSilent(false);
		player.unlock();
		if (getRenderAnimation() != null) {
			player.getAppearance().resetRenderAnimation();
		}
		if (getEndMessage(success) != null) {
			player.sendMessage(getEndMessage(success));
		} else if (getFilterableEndMessage(success) != null) {
			player.sendFilteredMessage(getFilterableEndMessage(success));
		}
		player.getSkills().addXp(SkillConstants.AGILITY, success ? getSuccessXp(object) : ((Failable) Shortcut.this).getFailXp(object));
		if (onComplete != null) {
			onComplete.accept(player);
		}
	}

	@Override
	default void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		handle(player, object, 0, null);
	}

}
