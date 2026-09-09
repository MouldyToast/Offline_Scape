package org.jesse.game.content.event.christmas2019;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 14/12/19
 */
public class ChristmasCupboard implements ObjectAction {
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        // land of snow cupboard returns the player to Edgeville
        new FadeScreen(player, () -> {
            player.setLocation(new Location(3094, 3487));
            WorldTasksManager.schedule(() -> AChristmasWarble.start(player), 1);
        }).fade(3);
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{
                ChristmasConstants.CHRISTMAS_CUPBOARD_ID
        };
    }
}
