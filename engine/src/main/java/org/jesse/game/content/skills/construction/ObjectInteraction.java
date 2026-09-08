package org.jesse.game.content.skills.construction;

import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.ObjectHandler;
import org.jesse.game.world.object.WorldObject;

public interface ObjectInteraction extends ObjectAction {

	@Override
    default void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		
	}
	
	void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option);

	@Override
    default void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
			player.stopAll();
			player.faceObject(object);
			if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
				return;
			}
			if (!(player.getControllerManager().getController() instanceof ConstructionController)) {
				return;
			}
			handleObjectAction(player, player.getConstruction(), player.getConstruction().getReference(object), object, optionId, option);
		}));
	}
	
}
