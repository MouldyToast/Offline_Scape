package org.jesse.plugins.object;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.Door;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Kris | 27/04/2019 02:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RangingGuildDoor implements ObjectAction {

    private static final Location OUTSIDE = new Location(3115, 3449, 0);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(!player.inArea("Ranging Guild") ? new Location(2657, 3439, 0) : new Location(2659, 3437, 0)), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getSkills().getLevelForXp(SkillConstants.RANGED) < 40) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Ranged level of at least 40 to enter the ranging guild."));
            return;
        }
        player.lock(3);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            private WorldObject door;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        door = Door.handleGraphicalDoor(object, null);
                        return;
                    case 1:
                        if (!player.inArea("Ranging Guild")) {
                            player.addWalkSteps(2659, 3437, 2, false);
                        } else {
                            player.addWalkSteps(2657, 3439, 2, false);
                        }
                        return;
                    case 3:
                        Door.handleGraphicalDoor(door, object);
                        stop();
                        return;
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GUILD_DOOR };
    }
}
