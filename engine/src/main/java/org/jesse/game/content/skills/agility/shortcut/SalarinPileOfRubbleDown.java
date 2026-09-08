package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SalarinPileOfRubbleDown implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(1);
        player.sendFilteredMessage("You climb down the pile of rubble...");
        player.setLocation(new Location(2616, 9571));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.PILE_OF_RUBBLE_23564
        };
    }
}
