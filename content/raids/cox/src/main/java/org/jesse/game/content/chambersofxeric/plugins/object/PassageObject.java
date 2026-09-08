package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.map.RaidArea;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 02:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PassageObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            final RaidArea room = raid.getRoom(player.getLocation());
            if (room == null) {
                return;
            }
            if (!room.canPass(player, object)) {
                return;
            }
            if (object.getRotation() == 1 || object.getRotation() == 3) {
                if (player.getY() > object.getY()) {
                    player.setLocation(new Location(object.getX(), object.getY() - 1, player.getPlane()));
                } else {
                    player.setLocation(new Location(object.getX(), object.getY() + 3, player.getPlane()));
                }
            } else {
                if (player.getX() < object.getX()) {
                    player.setLocation(new Location(object.getX() + 3, object.getY(), player.getPlane()));
                } else {
                    player.setLocation(new Location(object.getX() - 1, object.getY(), player.getPlane()));
                }
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.PASSAGE_29789 };
    }
}
