package org.jesse.plugins.object;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.ObjectHandler;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 6 jun. 2018 | 17:49:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FishingGuildEntranceObject implements ObjectAction {

    private static final WorldObject OPENED_DOOR = new WorldObject(20925, 0, 0, new Location(2611, 3394, 0));

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object.getY() > player.getY() ? new Location(2611, 3393, 0) : object), () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }));
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getSkills().getLevel(SkillConstants.FISHING) < 68) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Fishing level of at least 68 to enter the guild."));
            return;
        }
        object.setLocked(true);
        World.spawnGraphicalDoor(OPENED_DOOR);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        player.addWalkSteps(player.getX(), player.getY() <= 3393 ? 3394 : 3393, 1, false);
                        break;
                    case 1:
                        World.spawnGraphicalDoor(World.getObjectWithType(OPENED_DOOR, object.getType()));
                        break;
                    case 2:
                        object.setLocked(false);
                        stop();
                        break;
                }
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_20925 };
    }
}
