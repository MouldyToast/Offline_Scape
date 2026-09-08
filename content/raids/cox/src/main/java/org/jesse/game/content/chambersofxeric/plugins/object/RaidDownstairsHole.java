package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.room.FloorEdgeRoom;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import static org.jesse.plugins.object.LadderOA.CLIMB_DOWN;

/**
 * @author Kris | 06/07/2019 04:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidDownstairsHole implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, FloorEdgeRoom.class, room -> {
            player.lock();
            player.setAnimation(CLIMB_DOWN);
            player.getPacketDispatcher().sendClientScript(1512);
            WorldTasksManager.schedule(() -> {
                if (raid.isDestroyed()) {
                    return;
                }
                player.getPacketDispatcher().sendClientScript(1513);
                final Location center = room.getBoundTile();
                player.setLocation(center);
                player.lock(2);
                for (int x = -2; x <= 2; x++) {
                    for (int y = -2; y <= 2; y++) {
                        final WorldObject obj = World.getObjectWithType(center.transform(x, y, 0), 10);
                        if (obj == null || (obj.getId() != 29995 && obj.getId() != 29996)) {
                            continue;
                        }
                        switch(obj.getRotation()) {
                            case 0:
                                WorldTasksManager.schedule(() -> player.addWalkSteps(center.getX(), center.getY() - 1, 1, true));
                                break;
                            case 1:
                                WorldTasksManager.schedule(() -> player.addWalkSteps(center.getX() - 1, center.getY(), 1, true));
                                break;
                            case 2:
                                WorldTasksManager.schedule(() -> player.addWalkSteps(center.getX(), center.getY() + 1, 1, true));
                                break;
                            default:
                                WorldTasksManager.schedule(() -> player.addWalkSteps(center.getX() + 1, center.getY(), 1, true));
                                break;
                        }
                        return;
                    }
                }
            });
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HOLE_29734, ObjectId.HOLE_29735 };
    }
}
