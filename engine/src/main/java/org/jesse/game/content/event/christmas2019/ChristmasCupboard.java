package org.jesse.game.content.event.christmas2019;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
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
        int x, y;
    
        if (object.getPositionHash() == ChristmasConstants.homeChristmasCupboardLocation.getPositionHash()) {
            if (player.getFollower() != null) {
                player.sendMessage("The Queen of Snow has forbidden all pets in her domain. You'll have to pick up your follower if you want to travel to the Land of Snow.");
                return;
            }
            // home cupboard
            x = 2070 + Utils.random(0, 1);
            y = 5401 + Utils.random(0, 2);
        } else {
            // land of snow cupboard
            x = 3094;
            y = 3487;
        }
    
        new FadeScreen(player, () -> {
            player.setLocation(new Location(x, y));
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
