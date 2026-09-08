package org.jesse.plugins.object;

import org.jesse.game.content.skills.agility.pyramid.area.AgilityPyramidArea;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class AgilityPyramidStairs implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.STAIRS_10857) {
            player.setLocation(AgilityPyramidArea.getHigherTile(player.getLocation().transform(Direction.NORTH, 3)));
        } else {
            player.setLocation(AgilityPyramidArea.getLowerTile(player.getLocation().transform(Direction.SOUTH, 3)));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRS_10857, ObjectId.STAIRS_10858 };
    }
}
