package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.room.FloorEdgeRoom;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import static org.jesse.plugins.object.LadderOA.CLIMB_UP;

/**
 * @author Kris | 06/07/2019 04:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidUpstairsRope implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, FloorEdgeRoom.class, room -> {
            player.lock();
            player.setAnimation(CLIMB_UP);
            player.getPacketDispatcher().sendClientScript(1512);
            WorldTasksManager.schedule(() -> {
                if (raid.isDestroyed()) {
                    return;
                }
                player.getPacketDispatcher().sendClientScript(1513);
                player.setLocation(room.getBoundTile());
                player.lock(1);
            });
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROPE_29995 };
    }
}
