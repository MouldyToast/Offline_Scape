package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import static com.near_reality.game.item.CustomObjectId.*;


public class AfkEntranceBarrier implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        Analytics.flagInteraction(player, Analytics.InteractionType.AFK_SKILLING);
        player.resetWalkSteps();
        player.addWalkSteps(object.getX(), object.getY(), 1, true);
        player.lock(player.hasWalkSteps() ? 2 : 1);
        WorldTasksManager.scheduleOrExecute(() -> {
            final Location destination = object.transform(movingWestOrEast(player, object), player.matches(object) ? 1 : 0);
            player.addWalkSteps(destination.getX(), destination.getY(), 1, false);
        }, player.hasWalkSteps() ? 1 : -1);

    }

    private Direction movingWestOrEast(Player player, WorldObject object) {
        if (player.getLocation().getX() < object.getLocation().getX())
            return Direction.WEST;
        else
            return Direction.EAST;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {AFK_GATE_L, AFK_GATE_R};
    }
}
